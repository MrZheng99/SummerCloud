package com.zj.register.core;

public class RegistryService {
    private RegistryProperties registryProperties;
    public RegistryService(){}
    public RegistryService(RegistryProperties registryProperties){
        this.registryProperties=registryProperties;
    }

    @Override
    public String toString() {
        return "RegistryService{" +
                "registry-addr=" + registryProperties. +
                '}';
    }
}
