package com.zj.register.core;

import com.zj.base.constants.CHCCenter;
import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.*;
import com.zj.base.util.SerializeUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
        Socket sk=SocketCenter.SEND_SOCKET_MAP.getOrDefault(serviceName,null);
        if(Objects.isNull(sk)){
            log.info("创建新的socket分发请求");
            sk = new Socket(serverInfo.getAddr(),serverInfo.getPort());
            SocketCenter.SEND_SOCKET_MAP.put(serviceName,sk);
        }
        rpcRequestEntity.setDataType(DataType.INVOKE);
        SerializeUtil.send(rpcRequestEntity,sk);
        //回送数据
        Socket socket = SocketCenter.getByRequestID(rpcRequestEntity.getRequestID());
        SerializeUtil.send(SerializeUtil.accept(RpcResponseEntity.class,sk),socket);
    }
    public void send0(RpcRequestEntity rpcRequestEntity) throws IOException {
        InvokeData invokeData = (InvokeData) rpcRequestEntity.getData();

        //回送数据
        ChannelHandlerContext ctx = CHCCenter.getByRequestID(rpcRequestEntity.getRequestID());

    }
}
