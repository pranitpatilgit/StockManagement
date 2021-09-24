package com.pranitpatil.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "application")
public class StockManagementProperties {

    private int lockingInterval;

    public int getLockingInterval() {
        return lockingInterval;
    }

    public void setLockingInterval(int lockingInterval) {
        this.lockingInterval = lockingInterval;
    }
}
