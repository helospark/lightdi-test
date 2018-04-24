package com.helospark.lightdi.test.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.it.testcontext1.TestContext1;
import com.helospark.lightdi.test.junit4.LightDiJUnitTestRunner;

@RunWith(LightDiJUnitTestRunner.class)
@TestMetaAnnotation(imports = TestContext1.class)
public class MetaAnnotationTest {
    @Autowired
    private TestContext1 bean;

    @Test
    public void test() {
        // GIVEN

        // WHEN

        // THEN
        assertThat(bean, is(not(nullValue())));
    }
}
