package com.helospark.lightdi.test.junit4;

import static com.helospark.lightdi.properties.Environment.HIGHEST_PROPERTY_ORDER;
import static com.helospark.lightdi.test.annotation.TestPropertySource.PROPERTIES_ATTRIBUTE_NAME;
import static com.helospark.lightdi.util.BeanNameGenerator.createBeanNameForStereotypeAnnotatedClass;
import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;

import com.helospark.lightdi.LightDiContext;
import com.helospark.lightdi.constants.LightDiConstants;
import com.helospark.lightdi.descriptor.ManualDependencyDescriptor;
import com.helospark.lightdi.exception.IllegalConfigurationException;
import com.helospark.lightdi.properties.PropertySourceHolder;
import com.helospark.lightdi.test.annotation.LightDiTest;
import com.helospark.lightdi.test.annotation.MockBean;
import com.helospark.lightdi.test.annotation.SpyBean;
import com.helospark.lightdi.test.annotation.TestPropertySource;
import com.helospark.lightdi.util.AnnotationUtil;
import com.helospark.lightdi.util.AutowirePostProcessor;
import com.helospark.lightdi.util.LightDiAnnotation;
import com.helospark.lightdi.util.ReflectionUtil;

public class LightDiJUnitTestRunner extends BlockJUnit4ClassRunner {
    private Class<?> clazz;
    private LightDiContext context;

    public LightDiJUnitTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.clazz = klass;
    }

    @Override
    protected Object createTest() throws Exception {
        Object result = super.createTest();
        LightDiAnnotation annotation = extractAnnotationFromClass(clazz);
        context = new LightDiContext();

        preinitializeMocks(result);
        initializeInlineProperties(clazz);

        String[] packages = annotation.getAttributeAs(LightDiTest.ROOT_PACKAGE_ATTRIBUTE_NAME, String[].class);
        Class<?>[] classes = annotation.getAttributeAs(LightDiTest.CLASSES_ATTRIBUTE_NAME, Class[].class);

        List<Class<?>> classesToAddToContext = new ArrayList<>();
        classesToAddToContext.addAll(asList(classes));
        classesToAddToContext.add(result.getClass());

        context.loadDependencies(asList(packages), classesToAddToContext);

        AutowirePostProcessor autowireSupportUtil = context.getAutowireSupportUtil();
        autowireSupportUtil.autowireFieldsTo(result);
        return result;
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock(method);
        statement = thenClearContext(statement);
        return statement;
    }

    private Statement thenClearContext(Statement statement) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } finally {
                    context.close();
                    context = null;
                }
            }

        };
    }

    private void initializeInlineProperties(Class<?> clazz2) {
        // TODO: this only works on test class, in the long term, we need to add another annotation processor to lightdi core, by modifying the
        // DI inside LightDI to add additional beans to the chain
        // for now, this will do it for the most common case
        List<String> properties = AnnotationUtil.getAnnotationsOfType(clazz2, TestPropertySource.class)
                .stream()
                .map(ann -> ann.getAttributeAs(PROPERTIES_ATTRIBUTE_NAME, String[].class))
                .flatMap(propertyArray -> Arrays.stream(propertyArray))
                .collect(Collectors.toList());
        Map<String, String> propertyMap = new HashMap<>();
        for (String property : properties) {
            int equalLocation = property.indexOf("=");
            if (equalLocation == -1) {
                throw new IllegalConfigurationException(
                        "TestPropertySource properties has to have an equal sign, but '" + property + "' does not have it");
            }
            String key = property.substring(0, equalLocation);
            String value = property.substring(equalLocation + 1);
            propertyMap.put(key, value);
        }
        PropertySourceHolder propertySourceHolder = new PropertySourceHolder(HIGHEST_PROPERTY_ORDER, propertyMap);
        context.addPropertySource(propertySourceHolder);
    }

    private void preinitializeMocks(Object result) {
        registerMockBeans(result, context);
        registerSpyBeans(result, context);
    }

    private void registerMockBeans(Object instance, LightDiContext context) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> AnnotationUtil.hasAnnotation(field, MockBean.class))
                .forEach(field -> registerAndAutowire(instance, context, field, clazz -> Mockito.mock(clazz)));
    }

    private void registerSpyBeans(Object instance, LightDiContext context) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> AnnotationUtil.hasAnnotation(field, SpyBean.class))
                .forEach(field -> registerAndAutowire(instance, context, field, clazz -> Mockito.spy(clazz)));
    }

    private void registerAndAutowire(Object instance, LightDiContext context, Field field, Function<Class<?>, Object> mockFactory) {
        Class<?> classToCreate = field.getType();
        ManualDependencyDescriptor dependencyDescriptor = ManualDependencyDescriptor.builder()
                .withClazz(classToCreate)
                .withIsLazy(false)
                .withIsPrimary(true)
                .withQualifier(createBeanNameForStereotypeAnnotatedClass(classToCreate))
                .withScope(LightDiConstants.SCOPE_SINGLETON)
                .build();
        Object mock = mockFactory.apply(classToCreate);
        context.registerSingleton(dependencyDescriptor, mock);
        ReflectionUtil.injectToField(field, instance, mock);
    }

    private LightDiAnnotation extractAnnotationFromClass(Class<?> clazz) {
        return AnnotationUtil.getSingleAnnotationOfType(clazz, LightDiTest.class);
    }
}
