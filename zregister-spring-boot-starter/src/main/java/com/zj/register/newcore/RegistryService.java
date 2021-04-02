package com.zj.register.newcore;

import com.zj.register.newcore.RegistryProperties;

public class RegistryService {
    private RegistryProperties registryProperties;
    public RegistryService(){}
    public RegistryService(RegistryProperties registryProperties){
        this.registryProperties=registryProperties;
    }

    @Override
    public String toString() {
        return "RegistryService{" +
                "registryProperties=" + registryProperties +
                '}';
    }
}
