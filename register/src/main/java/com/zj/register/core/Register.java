package com.zj.register.core;


import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.util.SerializeUtil;
import com.zj.register.conf.RegisterConfig;
import com.zj.rpc.monitor.Monitor;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * @author zj
 * @date 2020/12/30 14:14
 */
public class Register {


    public static void register(RegisterConfigDefinition registerRCD,ServiceConfigDefinition serviceRCD) throws IOException {
        final boolean identifyEnable = registerRCD.isIdentifyEnable();
        if(identifyEnable) {
            if(Objects.isNull(RegisterConfig.CONF)){
                RegisterConfig.CONF = registerRCD;
            }
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
