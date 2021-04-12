package com.zj.register.core;


import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.util.SerializeUtil;
import com.zj.rpc.monitor.Monitor;

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
    public void register(RegisterConfigDefinition registerRCD) throws IOException {
        final boolean identifyEnable = registerRCD.isIdentifyEnable();
        if(identifyEnable) {
            Monitor monitor=new Monitor();
            monitor.start();
            return;
        }
        final String addr=registerRCD.getAddr();
        final int port=registerRCD.getPort();
        Socket sk=null;
        try {
            sk= new Socket(addr,port);
            ServerInfo serverInfo = new ServerInfo(serviceRCD.getName(), serviceRCD.getAddr(), serviceRCD.getPort());
            SerializeUtil.send(new RpcRequestEntity(DataType.REGISTER,serverInfo),sk);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            assert sk != null;
            sk.close();
        }
    }
}
