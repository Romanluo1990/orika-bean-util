package roman.common.util.bean.orika;

import ma.glasnost.orika.*;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ExceptionUtility;
import ma.glasnost.orika.impl.MapperFacadeImpl;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;
import roman.common.util.bean.orika.annotation.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/** 注解式懒加载MapperFacade */
public class LazyMapperFacade extends MapperFacadeImpl {


	private final Set<java.lang.reflect.Type> initCache = new HashSet<>();

	/**
	 * Constructs a new MapperFacadeImpl
	 *
	 * @param mapperFactory
	 * @param contextFactory
	 * @param unenhanceStrategy
	 * @param exceptionUtil
	 */
	public LazyMapperFacade(MapperFactory mapperFactory, MappingContextFactory contextFactory,
			UnenhanceStrategy unenhanceStrategy, ExceptionUtility exceptionUtil) {
		super(mapperFactory, contextFactory, unenhanceStrategy, exceptionUtil);
	}

	@Override
	public <S, D> MappingStrategy resolveMappingStrategy(S sourceObject,
			java.lang.reflect.Type initialSourceType,
			java.lang.reflect.Type initialDestinationType, boolean mapInPlace,
			MappingContext context) {
		Type<D> destInationType = TypeFactory.valueOf(initialDestinationType);
		Class<D> destClass = destInationType.getRawType();
		Map<String, ClassMapBuilder> classMapBuilders = new HashMap<>();
		registerClassMapperByAnnotation(destClass,classMapBuilders);
		Class orgiClass = sourceObject.getClass();
		registerClassMapperByAnnotation(orgiClass,classMapBuilders);
		classMapBuilders.values().stream().map(ClassMapBuilder::byDefault)
						.forEach(ClassMapBuilder::register);
		return super
				.resolveMappingStrategy(sourceObject, initialSourceType, initialDestinationType, mapInPlace, context);
	}

	/**
	 * 通过Annotation增加Mapper
	 *
	 * @param clazz
	 * @param classMapBuilders
	 */
	private void registerClassMapperByAnnotation(Class<?> clazz,
			Map<String, ClassMapBuilder> classMapBuilders) {
		if (!initCache.contains(clazz)) {
			Type<?> classType = TypeFactory.valueOf(clazz);
			synchronized (classType) {
				if (!initCache.contains(clazz)) {

					// 注册ClassMap
					ClassMap classMap = clazz.getAnnotation(ClassMap.class);
					if (classMap != null) {
						Arrays.stream(classMap.converter()).forEach(this::registerConverter);
						Arrays.stream(classMap.mapper()).forEach(this::registerMapper);
						Arrays.stream(classMap.filter()).forEach(this::registerFilter);
						Arrays.stream(classMap.mapNullsDisable())
							  .forEach(initMapNullsDisable(clazz, classMapBuilders));
					}
					// 注册FieldMap
					List<ClassField> classFields = getAllClassFields(clazz);
					classFields.forEach(initFieldMapper(classMapBuilders));
					initCache.add(clazz);
				}
			}
		}
	}

	private Consumer<ClassField> initFieldMapper(
			Map<String, ClassMapBuilder> classMapBuilders) {
		return classField -> {
			Field field = classField.getField();
			Class clazz = classField.getClazz();
			List<FieldMap> fieldMapList = new ArrayList<>();
			DestFieldMaps destFieldMaps = field.getAnnotation(DestFieldMaps.class);
			if (destFieldMaps != null) {
				Arrays.stream(destFieldMaps.value()).map(destToFieldMap(clazz, field)).forEach(fieldMapList::add);
			}
			DestFieldMap destFieldMap = field.getAnnotation(DestFieldMap.class);
			if (destFieldMap != null) {
				fieldMapList.add(destToFieldMap(clazz, field).apply(destFieldMap));
			}
			OrigFieldMaps origFieldMaps = field.getAnnotation(OrigFieldMaps.class);
			if (origFieldMaps != null) {
				Arrays.stream(origFieldMaps.value()).map(origToFieldMap(clazz, field)).forEach(fieldMapList::add);
			}
			OrigFieldMap origFieldMap = field.getAnnotation(OrigFieldMap.class);
			if (origFieldMap != null) {
				fieldMapList.add(origToFieldMap(clazz, field).apply(origFieldMap));
			}
			if (fieldMapList.isEmpty()) {
				return;
			}
			fieldMapList.forEach(addFieldMapBuilder(classMapBuilders));
		};
	}

	private Function<DestFieldMap, FieldMap> destToFieldMap(Class<?> clazz, Field field) {
		return destFieldMap -> {
			FieldMap fieldMap = new FieldMap();
			fieldMap.setOrigClass(destFieldMap.origClass());
			fieldMap.setDestClass(clazz);
			fieldMap.setOrigField(destFieldMap.origField());
			fieldMap.setDestField(field.getName());
			fieldMap.setExclude(destFieldMap.exclude());
			fieldMap.setMapNulls(destFieldMap.mapNulls());
			fieldMap.setConverter(destFieldMap.converter());
			fieldMap.setByDefault(destFieldMap.byDefault());
			return fieldMap;
		};
	}

	private Function<OrigFieldMap, FieldMap> origToFieldMap(Class<?> clazz, Field field) {
		return origFieldMap -> {
			FieldMap fieldMap = new FieldMap();
			fieldMap.setOrigClass(clazz);
			fieldMap.setDestClass(origFieldMap.destClass());
			fieldMap.setOrigField(field.getName());
			fieldMap.setDestField(origFieldMap.destField());
			fieldMap.setExclude(origFieldMap.exclude());
			fieldMap.setMapNulls(origFieldMap.mapNulls());
			fieldMap.setConverter(origFieldMap.converter());
			fieldMap.setByDefault(origFieldMap.byDefault());
			return fieldMap;
		};
	}

	private Consumer<? super FieldMap> addFieldMapBuilder(Map<String, ClassMapBuilder> classMapBuilders) {
		return fieldMap -> {
			Class<?> classA = fieldMap.getOrigClass();
			String orgiField = fieldMap.getOrigField();
			Class<?> classB = fieldMap.getDestClass();
			String destField = fieldMap.getDestField();
			if (orgiField == null || orgiField == "") {
				orgiField = destField;
			}
			if (destField == null || destField == "") {
				destField = orgiField;
			}
			Class<? extends Converter<?, ?>> converter = fieldMap.getConverter();
			registerConverter(converter);
			boolean byDefault = fieldMap.isByDefault();
			boolean mapNulls = fieldMap.isMapNulls();
			boolean exclude = fieldMap.isExclude();
			ClassMapBuilder classMapBuilder = classMapBuilders.get(classA.getName() + classB.getName());
			if (classMapBuilder == null) {
				classMapBuilder = mapperFactory.classMap(classA, classB);
				classMapBuilders.put(classA.getName() + classB.getName(), classMapBuilder);
			}
			FieldMapBuilder fieldMapBuilder = classMapBuilder.fieldMap(orgiField, destField, byDefault)
															 .mapNulls(mapNulls);
			if (exclude) {
				fieldMapBuilder.exclude();
			}
			fieldMapBuilder.add();
		};
	}

	private List<ClassField> getAllClassFields(Class<?> clazz) {
		List<ClassField> classFieldList = new ArrayList<>();
		List<Class> progenyClazzs = new ArrayList<>();
		progenyClazzs.add(clazz);
		Class tempClass = clazz;
		while (tempClass != null && !Object.class.equals(tempClass)) { // 当父类为null的时候说明到达了最上层的父类(Object类).
			Arrays.stream(tempClass.getDeclaredFields()).forEach(filed -> {
				progenyClazzs.stream()
							 .map(progenyClazz -> new ClassField(progenyClazz, filed))
							 .forEach(classFieldList::add);
			});
			tempClass = tempClass.getSuperclass(); // 得到父类,然后赋给自己
			progenyClazzs.add(tempClass);
		}
		return classFieldList;
	}

	private void registerConverter(Class<? extends Converter<?, ?>> converter) {
		if (VoidConverter.class.equals(converter)) {
			return;
		}
		ConverterFactory converterFactory = mapperFactory.getConverterFactory();
		String converterName = converter.getName();
		if (!converterFactory.hasConverter(converterName)) {
			synchronized (converter.getClass()) {
				if (!converterFactory.hasConverter(converterName)) {
					try {
						converterFactory.registerConverter(converterName, converter.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						throw new IllegalStateException("converter can't instance", e);
					}
				}
			}
		}
	}

	private void registerMapper(Class<? extends Mapper<?, ?>> mapper) {
		try {
			mapperFactory.registerMapper(mapper.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("mapper can't instance", e);
		}
	}

	private void registerFilter(Class<? extends Filter<?, ?>> filter) {
		try {
			mapperFactory.registerFilter(filter.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("filter can't instance", e);
		}
	}

	private Consumer<Class<?>> initMapNullsDisable(Class<?> classB,
			Map<String, ClassMapBuilder> classMapBuilders) {
		return classA -> {
			ClassMapBuilder classMapBuilder = classMapBuilders.get(classA.getName() + classB.getName());
			if (classMapBuilder == null) {
				classMapBuilder = mapperFactory.classMap(classA, classB);
				classMapBuilders.put(classA.getName() + classB.getName(), classMapBuilder);
			}
			classMapBuilder.mapNulls(false);
		};
	}
}
