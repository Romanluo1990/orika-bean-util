package roman.common.util.bean.orika.annotation;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Filter;
import ma.glasnost.orika.Mapper;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClassMap {

	Class<? extends Converter<?, ?>>[] converter() default {};

	Class<? extends Mapper<?, ?>>[] mapper() default {};

	Class<? extends Filter<?, ?>>[] filter() default {};

	Class<?>[] mapNullsDisable() default {};
}
