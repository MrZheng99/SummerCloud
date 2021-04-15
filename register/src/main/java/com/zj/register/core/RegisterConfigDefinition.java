package com.zj.register.core;

import com.zj.base.entity.LoadBalanceType;
import com.zj.base.exception.NotFoundLoadBalanceType;
import lombok.Data;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zj
 * @date 2020/12/30 13:59
 */
@Data
public class RegisterConfigDefinition {
    private String addr;//监听地址
    private int port;//监听端口
    private String name;//配置名称
    private LoadBalanceType loadBalanceType;//负载均衡类型
    private boolean identifyEnable;//是否注册自己

    public static RegisterConfigDefinition readConfig(Class<?> clazz, String confPath, String prefix) throws IOException {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(confPath));
        RegisterConfigDefinition rcd = new RegisterConfigDefinition();
        boolean identifyEnable = "true".equals(properties.getProperty(prefix + ".identifyEnable"));
        String property = properties.getProperty(prefix + ".loadBalance");
        LoadBalanceType loadBalanceType = null;
        if (property == null || "".equals(property)) {
            loadBalanceType = LoadBalanceType.RU;
        } else if ("random".equalsIgnoreCase(property)) {
            loadBalanceType = LoadBalanceType.RANDOM;
        } else if ("poll".equalsIgnoreCase(property)) {
            loadBalanceType = LoadBalanceType.POLL;
        } else if ("ru".equalsIgnoreCase(property)) {
            loadBalanceType = LoadBalanceType.RU;
        } else {
            throw new NotFoundLoadBalanceType("暂不支持负载均衡类型：" + property);
        }
        rcd.setLoadBalanceType(loadBalanceType);
        rcd.setIdentifyEnable(identifyEnable);
        if (identifyEnable) {
            return rcd;
        }
        int port = Integer.parseInt(properties.getProperty(prefix + ".port"));
        String addr = properties.getProperty(prefix + ".addr");
        String name = properties.getProperty("zj.name");
        rcd.setAddr(addr);
        rcd.setName(name);
        rcd.setPort(port);
        return rcd;
    }

}
