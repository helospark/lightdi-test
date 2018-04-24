package com.helospark.lightdi.test.annotation;

import static com.helospark.lightdi.properties.Environment.HIGHEST_PROPERTY_ORDER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.helospark.lightdi.annotation.AliasFor;
import com.helospark.lightdi.annotation.PropertySource;

@Retention(RUNTIME)
@Documented
@PropertySource(value = "ignored")
@Target(TYPE)
public @interface TestPropertySource {
    @AliasFor(annotation = PropertySource.class, value = "value")
    String[] locations();

    @AliasFor(annotation = PropertySource.class, value = "order")
    int order() default HIGHEST_PROPERTY_ORDER;
}
