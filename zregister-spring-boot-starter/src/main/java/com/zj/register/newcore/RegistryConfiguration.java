package com.zj.register.newcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
public class RegistryConfiguration {
    @Autowired
    private RegistryProperties registryProperties;

}
