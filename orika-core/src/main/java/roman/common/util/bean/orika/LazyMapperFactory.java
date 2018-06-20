package roman.common.util.bean.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContextFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;

public class LazyMapperFactory extends DefaultMapperFactory {

    /**
     * Constructs a new instance of DefaultMapperFactory
     *
     * @param builder
     */
    public LazyMapperFactory(MapperFactoryBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    protected MapperFacade buildMapperFacade(MappingContextFactory contextFactory, UnenhanceStrategy unenhanceStrategy) {
        return new LazyMapperFacade(this,contextFactory, unenhanceStrategy, exceptionUtil);
    }
}
