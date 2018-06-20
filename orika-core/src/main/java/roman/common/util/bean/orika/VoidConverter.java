package roman.common.util.bean.orika;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * 无效转换标志类
 */
public class VoidConverter extends CustomConverter<Object,Object> {

    @Override
    public Object convert(Object source, Type<?> destinationType, MappingContext mappingContext) {
        throw new UnsupportedOperationException("标识类不实现运行方法");
    }
}
