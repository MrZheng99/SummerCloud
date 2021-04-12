package com.zj.register.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.*;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * 负责转发请求到具体服务器执行
 */
@Slf4j
public class Send {
    public void send(RpcRequestEntity rpcRequestEntity) throws IOException {
        InvokeData invokeData = (InvokeData) rpcRequestEntity.getData();
        String serviceName = invokeData.getServiceName();
        ServerInfo serverInfo= RegisterCenter.getByName(serviceName);
        log.info("serverInfo:{}",serverInfo);
        Socket sk = new Socket(serverInfo.getAddr(),serverInfo.getPort());
        rpcRequestEntity.setDataType(DataType.INVOKE);
        SerializeUtil.send(rpcRequestEntity,sk);
        //回送数据
        Socket socket = SocketCenter.getByRequestID(rpcRequestEntity.getRequestID());
        SerializeUtil.send(SerializeUtil.accept(RpcResponseEntity.class,sk,true),socket);
    }
}
