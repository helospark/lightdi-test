package com.helospark.lightdi.test.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.property.PropertyContext;
import com.helospark.lightdi.test.junit4.LightDiJUnitTestRunner;

@RunWith(LightDiJUnitTestRunner.class)
@LightDiTest(classes = PropertyContext.class)
@TestPropertySource(locations = "classpath:test-overrides.properties")
public class TestPropertySourceTest {
    @Autowired
    private PropertyContext context;

    @Test
    public void testPropertyHasBeenOverridden() {
        // GIVEN

        // WHEN

        // THEN
        assertThat(context.getPropertyValue(), is("overridden"));
    }

}
