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
    public static final String PROPERTIES_ATTRIBUTE_NAME = "properties";

    /**
     * Property override locations.
     * @return locations either in classpath, or file
     */
    @AliasFor(annotation = PropertySource.class, value = "value")
    String[] locations() default {};

    /**
     * Order of the property source, by default highest.
     * @return order
     */
    @AliasFor(annotation = PropertySource.class, value = "order")
    int order() default HIGHEST_PROPERTY_ORDER;

    /**
     * Add list of properties to property source.
     * Note: that due to a limitation in 0.0.2, this only works on the test class
     * @return list of properties
     */
    String[] properties() default {};
}
