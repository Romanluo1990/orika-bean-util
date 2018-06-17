package roman.common.util.bean.orika;

import ma.glasnost.orika.*;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ExceptionUtility;
import ma.glasnost.orika.impl.MapperFacadeImpl;
import ma.glasnost.orika.metadata.FieldMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;
import roman.common.util.bean.orika.annotation.ClassMap;
import roman.common.util.bean.orika.annotation.FieldMap;
import roman.common.util.bean.orika.annotation.FieldMaps;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

/**
 * 注解式懒加载MapperFacade
 */
public class LazyMapperFacade extends MapperFacadeImpl {

    private Set<java.lang.reflect.Type> initCache = new HashSet<>();

    /**
     * Constructs a new MapperFacadeImpl
     *
     * @param mapperFactory
     * @param contextFactory
     * @param unenhanceStrategy
     * @param exceptionUtil
     */
    public LazyMapperFacade(MapperFactory mapperFactory, MappingContextFactory contextFactory, UnenhanceStrategy unenhanceStrategy, ExceptionUtility exceptionUtil) {
        super(mapperFactory, contextFactory, unenhanceStrategy, exceptionUtil);
    }

    @Override
    public <S, D> MappingStrategy resolveMappingStrategy(S sourceObject, java.lang.reflect.Type initialSourceType, java.lang.reflect.Type initialDestinationType, boolean mapInPlace, MappingContext context) {
        Type<D> destinationType = TypeFactory.valueOf(initialDestinationType);
        Class<D> descClass = destinationType.getRawType();
        registerClassMapperByAnnotation(descClass);
        return super.resolveMappingStrategy(sourceObject, initialSourceType, initialDestinationType, mapInPlace, context);
    }

    /**
     * 通过Annotation增加Mapper
     * @param descClass
     */
    public void registerClassMapperByAnnotation(Class<?> descClass) {
        if(!initCache.contains(descClass)) {
            Type<?> destinationType = TypeFactory.valueOf(descClass);
            synchronized (destinationType) {
                if (!initCache.contains(descClass)) {
                    //注册ClassMap
                    ClassMap classMap = descClass.getAnnotation(ClassMap.class);
                    if (classMap != null) {
                        Arrays.stream(classMap.converter()).forEach(this::registerConverter);
                        Arrays.stream(classMap.mapper()).forEach(this::registerMapper);
                        Arrays.stream(classMap.filter()).forEach(this::registerFilter);
                        Arrays.stream(classMap.mapNullsDisable()).forEach(initMapNullsDisable(descClass));
                    }
                    //注册FieldMap
                    List<Field> fields = getAllFields(descClass);
                    fields.forEach(initFieldMapper(descClass));

                    initCache.add(descClass);
                }
            }
        }

    }

    private Consumer<Field> initFieldMapper(Class<?> classB) {
        return field -> {
            List<FieldMap> fieldMapList;
            FieldMaps fieldMaps = field.getAnnotation(FieldMaps.class);
            if(fieldMaps != null)
                fieldMapList= Arrays.asList(fieldMaps.value());
            else
                fieldMapList = new ArrayList<>();
            FieldMap fieldMap = field.getAnnotation(FieldMap.class);
            if(fieldMap != null)
                fieldMapList.add(fieldMap);
            if(fieldMapList.isEmpty())
                return;
            String destField = field.getName();
            fieldMapList.parallelStream().forEach(registerFieldMap(classB,destField));
        };
    }

    private Consumer<? super FieldMap> registerFieldMap(Class<?> classB, String destField) {
        return fieldMap -> {
            Class<?> classA = fieldMap.origClass();
            String orgiField = fieldMap.origField();
            if("".equals(orgiField)){
                orgiField = destField;
            }
            Class<? extends Converter<?, ?>> converter = fieldMap.converter();
            registerConverter(converter);
            boolean byDefault = fieldMap.byDefault();
            boolean mapNulls = fieldMap.mapNulls();
            boolean exclude = fieldMap.exclude();
            FieldMapBuilder fieldMapBuilder = mapperFactory.classMap(classA,classB)
                    .fieldMap(orgiField,destField,byDefault)
                    .mapNulls(mapNulls);
            if(exclude)
                fieldMapBuilder.exclude();
            fieldMapBuilder.add().byDefault().register();
        };
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = clazz;
        while (tempClass != null && !Object.class.equals(tempClass) ) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    private void registerConverter(Class<? extends Converter<?,?>> converter) {
        if(VoidConverter.class.equals(converter))
            return;
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        String converterName = converter.getName();
        if(!converterFactory.hasConverter(converterName)){
            synchronized (converter.getClass()){
                if(!converterFactory.hasConverter(converterName)){
                    try {
                        converterFactory.registerConverter(converterName, converter.newInstance());
                    } catch (InstantiationException|IllegalAccessException e) {
                        throw new IllegalStateException("converter can't instance",e);
                    }
                }
            }
        }
    }

    private void registerMapper(Class<? extends Mapper<?,?>> mapper) {
        try {
            mapperFactory.registerMapper(mapper.newInstance());
        } catch (InstantiationException|IllegalAccessException e) {
            throw new IllegalStateException("mapper can't instance",e);
        }
    }

    private void registerFilter(Class<? extends Filter<?,?>> filter) {
        try {
            mapperFactory.registerFilter(filter.newInstance());
        } catch (InstantiationException|IllegalAccessException e) {
            throw new IllegalStateException("filter can't instance",e);
        }
    }

    private Consumer<Class<?>> initMapNullsDisable(Class<?> classB) {
        return classA -> mapperFactory.classMap(classA,classB).mapNulls(false).byDefault().register();
    }
}
