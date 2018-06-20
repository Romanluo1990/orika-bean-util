package roman.common.util.bean;

import ma.glasnost.orika.impl.DefaultMapperFactory;
import roman.common.util.bean.orika.LazyMapperFacade;
import roman.common.util.bean.orika.LazyMapperFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BeanUtilsBean {

    private LazyMapperFactory mapperFactory;

    private LazyMapperFacade mapper;

    public BeanUtilsBean() {
        mapperFactory = new LazyMapperFactory(new DefaultMapperFactory.Builder());
        mapper = (LazyMapperFacade)mapperFactory.getMapperFacade();
    }

    public <S, D> D map(S orig, Class<D> destClass){
        return mapper.map(orig,destClass);
    }

    public <S, D> void copyProperties(S orig, D dest){
        mapper.map(orig,dest);
    }

    public <S> Map<String,Object> describe (S orig){
        return map(orig,Map.class);
    }

    public <S, D> List<D> mapAsList(Iterable<S> origs, Class<D> destClass){
        return mapper.mapAsList(origs,destClass);
    }

    public <S, D> void mapToCollection(Iterable<S> origs, Class<D> destClass, Collection<D> collection){
        mapper.mapAsCollection(origs,collection,destClass);
    }

    public void registerClassMapperByAnnotation(Class<?> descClass) {
        mapper.registerClassMapperByAnnotation(descClass);
    }
}
