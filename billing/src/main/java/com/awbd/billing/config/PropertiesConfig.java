package com.awbd.billing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("billings")
@Getter
@Setter
public class PropertiesConfig {
    private int amount;
    private boolean debt;
}
