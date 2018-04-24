package com.helospark.lightdi.test.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.helospark.lightdi.annotation.Configuration;

@Retention(RUNTIME)
@Target(TYPE)
@Configuration
public @interface LightDiTest {
    public static final String ROOT_PACKAGE_ATTRIBUTE_NAME = "rootPackage";
    public static final String CLASSES_ATTRIBUTE_NAME = "classes";

    /**
     * Root packages to component scan and initialize component scan with.
     * @return root packages
     */
    String[] rootPackage() default {};

    /**
     * Classes to initialize test context with.
     * @return classes
     */
    Class<?>[] classes() default {};

    /**
     * By default the test class is also part of the context, therefore applicable for autowire, beanselection, etc.
     * This should not cause issues in most cases, but if the test class implements an interface, that is injected in the appcontext, consider 
     * turning it off.
     * Note: turning it off means, that all LightDi annotations (like TestPropertySource, Import, etc.) will stop working on this class, in that case, you
     * should move them to a separate class, and add that class to LightDiTest.classes.
     * @return Whether to add the test class to the LightDiContext.
     */
    boolean addTestClassToContext() default true;
}
