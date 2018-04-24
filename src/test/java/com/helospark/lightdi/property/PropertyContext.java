package com.helospark.lightdi.property;

import com.helospark.lightdi.annotation.Configuration;
import com.helospark.lightdi.annotation.PropertySource;
import com.helospark.lightdi.annotation.Value;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertyContext {

    @Value("${TEST_PROPERTY}")
    private String propertyValue;

    public String getPropertyValue() {
        return propertyValue;
    }

}
