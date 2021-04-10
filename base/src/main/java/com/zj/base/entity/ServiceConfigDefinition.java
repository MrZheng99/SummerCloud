package com.zj.base.entity;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zj
 * @date 2020/12/31 11:40
 */
public class ServiceConfigDefinition {
    private  String addr;//监听地址
    private  int port;//监听端口
    private  String name;//配置名称

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceConfigDefinition{" +
                "addr='" + addr + '\'' +
                ", port=" + port +
                ", name='" + name + '\'' +
                '}';
    }

    public ServiceConfigDefinition(String addr, int port, String name) {
        this.addr = addr;
        this.port = port;
        this.name = name;
    }

    public ServiceConfigDefinition() {
    }
    public static ServiceConfigDefinition readConfig(Class<?> clazz,String confPath,String prefix) throws IOException {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(confPath));
        ServiceConfigDefinition scd = new ServiceConfigDefinition();
        int port= Integer.parseInt(properties.getProperty(prefix+".port"));
        String addr=properties.getProperty(prefix+".addr");
        String name=properties.getProperty(prefix+".name");
        scd.setAddr(addr);
        scd.setName(name);
        scd.setPort(port);
        return scd;
    }
}
