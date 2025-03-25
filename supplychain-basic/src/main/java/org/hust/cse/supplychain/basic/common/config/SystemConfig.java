package org.hust.cse.supplychain.basic.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system")
@Data
public class SystemConfig {
    private String url;
    private String evidenceAddress;
}

