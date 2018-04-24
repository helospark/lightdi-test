package com.helospark.lightdi.test.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.helospark.lightdi.annotation.AliasFor;
import com.helospark.lightdi.annotation.Import;

@LightDiTest
@Retention(RUNTIME)
@Target(TYPE)
@Import(Object.class)
public @interface TestMetaAnnotation {

    @AliasFor(annotation = Import.class, value = "value")
    Class<?>[] imports();

}
