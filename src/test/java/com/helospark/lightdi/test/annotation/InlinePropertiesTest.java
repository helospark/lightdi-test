package com.helospark.lightdi.test.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Value;
import com.helospark.lightdi.property.PropertyContext;
import com.helospark.lightdi.test.junit4.LightDiJUnitTestRunner;

@RunWith(LightDiJUnitTestRunner.class)
@LightDiTest(classes = PropertyContext.class)
@TestPropertySource(properties = { "TEST_ONLY_PROPERTY=yes",
        "TEST_PROPERTY=overridden" })
public class InlinePropertiesTest {
    @Autowired
    private PropertyContext propertyContext;
    @Value("${TEST_ONLY_PROPERTY}")
    private String testOnlyProperty;

    @Test
    public void testOverridingProperties() {
        // GIVEN

        // WHEN

        // THEN
        assertThat(propertyContext.getPropertyValue(), is("overridden"));
    }

    @Test
    public void testTestOnlyProperties() {
        // GIVEN

        // WHEN

        // THEN
        assertThat(testOnlyProperty, is("yes"));
    }
}
