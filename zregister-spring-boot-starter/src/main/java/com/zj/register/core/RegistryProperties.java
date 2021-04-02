package com.zj.register.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zj.registry")
public class RegistryProperties {
    private String addr;//注册中心地址
    private int port;//注册中心端口
}
