package com.zj.register.core;


import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.util.SerializeUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.registry.Registry;

/**
 * @author zj
 * @date 2020/12/30 14:14
 */
public class Register {
    private final ServiceConfigDefinition serviceRCD;

    public  Register(ServiceConfigDefinition serviceRCD ){
        this.serviceRCD=serviceRCD;
    }
//    /**
//     * 启动服务并注册到注册中心去，并识别自己是否为注册中心
//     * @param clazz 当前运行的类
//     * @throws IOException
//     */
//    public void register(Class<?> clazz,String confPath,String prefix) throws IOException {
//        RegisterConfigDefinition registerRCD = RegisterConfigDefinition.readConfig(clazz,confPath,prefix);
//        final boolean identifyEnable = registerRCD.isIdentifyEnable();
//        if(identifyEnable) {
//            return;
//        }
//        final String addr=registerRCD.getAddr();
//        final int port=registerRCD.getPort();
//        Socket sk=null;
//        OutputStream outputStream=null;
//        try {
//            sk= new Socket(addr,port);
//            outputStream = sk.getOutputStream();
//            ServerInfo serverInfo = new ServerInfo(serviceRCD.getName(), serviceRCD.getAddr(), serviceRCD.getPort());
//            SerializeUtil.send(new RpcRequestEntity(DataType.REGISTER,serverInfo),outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            assert sk != null;
//            sk.close();
//        }
//    }
    public void register(RegisterConfigDefinition registerRCD) throws IOException {
        final boolean identifyEnable = registerRCD.isIdentifyEnable();
        if(identifyEnable) {
            return;
        }
        final String addr=registerRCD.getAddr();
        final int port=registerRCD.getPort();
        Socket sk=null;
        OutputStream outputStream=null;
        try {
            sk= new Socket(addr,port);
            outputStream = sk.getOutputStream();
            ServerInfo serverInfo = new ServerInfo(serviceRCD.getName(), serviceRCD.getAddr(), serviceRCD.getPort());
            SerializeUtil.send(new RpcRequestEntity(DataType.REGISTER,serverInfo),outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            assert sk != null;
            sk.close();
        }
    }
}
