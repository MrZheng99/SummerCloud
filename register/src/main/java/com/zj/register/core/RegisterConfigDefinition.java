package com.zj.register.core;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zj
 * @date 2020/12/30 13:59
 */
public class RegisterConfigDefinition {
    private  String addr;//监听地址
    private  int port;//监听端口
    private  String name;//配置名称
    private  boolean identifyEnable;//是否注册自己


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

    public boolean isIdentifyEnable() {
        return identifyEnable;
    }

    public void setIdentifyEnable(boolean identifyEnable) {
        this.identifyEnable = identifyEnable;
    }

    @Override
    public String toString() {
        return "com.zj.register.core.RegisterConfigDefinition{" +
                "addr='" + addr + '\'' +
                ", port=" + port +
                ", name='" + name + '\'' +
                ", identifyEnable=" + identifyEnable +
                '}';
    }
    public static RegisterConfigDefinition readConfig(Class<?> clazz,String confPath,String prefix) throws IOException {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(confPath));
        RegisterConfigDefinition rcd = new RegisterConfigDefinition();
        boolean identifyEnable= "true".equals(properties.getProperty(prefix+".identifyEnable"));
        rcd.setIdentifyEnable(identifyEnable);
        if (identifyEnable) {
            return rcd;
        }
        int port= Integer.parseInt(properties.getProperty(prefix+".port"));
        String addr=properties.getProperty(prefix+".addr");
        String name=properties.getProperty("zj.name");
        rcd.setAddr(addr);
        rcd.setName(name);
        rcd.setPort(port);
        return rcd;
    }

}
