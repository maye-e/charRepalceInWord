package com.may.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "may",ignoreInvalidFields = true)
public class PropVO {
    private String directory;
    private Boolean showDetail;
    private Boolean caseSensitive;
    private Map<String,String> replaces;
}
