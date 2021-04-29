package com.zj.base.entity;

import lombok.Data;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zj
 * @date 2020/12/31 11:40
 */
@Data
public class ServiceConfigDefinition {
    private  String addr;//监听地址
    private  int port;//监听端口
    private  String name;//配置名称
    private  String user;//配置名称
    private  String password;//配置名称



    public static ServiceConfigDefinition readConfig(Class<?> clazz,String confPath,String prefix) throws IOException {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(confPath));
        ServiceConfigDefinition scd = new ServiceConfigDefinition();
        int port= Integer.parseInt(properties.getProperty(prefix+".port"));
        String addr=properties.getProperty(prefix+".addr");
        String name=properties.getProperty(prefix+".name");
        String user=properties.getProperty(prefix+".user");
        String password=properties.getProperty(prefix+".password");
        scd.setAddr(addr);
        scd.setName(name);
        scd.setPort(port);
        scd.setUser(user);
        scd.setPassword(password);
        return scd;
    }
}
