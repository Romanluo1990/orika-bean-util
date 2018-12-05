package roman.common.util.bean.orika.annotation;

import java.lang.annotation.*;

/** 多FieldMap */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DestFieldMaps {

	DestFieldMap[] value() default {};
}
