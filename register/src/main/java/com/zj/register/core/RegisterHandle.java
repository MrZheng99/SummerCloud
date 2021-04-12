package com.zj.register.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 负责注册中心的各个功能分发
 */
@Slf4j
public class RegisterHandle implements Runnable{
    private final Socket socket;

    public RegisterHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            RpcRequestEntity rpcRequestEntity = SerializeUtil.accept(RpcRequestEntity.class, socket);
            switch (rpcRequestEntity.getDataType()) {
                case REGISTER:
                    ServerInfo serverInfo = (ServerInfo) rpcRequestEntity.getData();
                    RegisterCenter.add(serverInfo.getName(), serverInfo);
                    log.info("已有服务[{}]", RegisterCenter.getServiceList());
                    is.close();
                    break;
                case SEND:
                    SocketCenter.add(rpcRequestEntity.getRequestID(), socket);
                    log.info("转发远程调用到具体服务器");
                    Send invokeSend = new Send();
                    invokeSend.send(rpcRequestEntity);
                    log.info("转发远程调用到具体服务器结束");
                    break;
                case GET_SERVICE_LIST:
                    log.info("获取已注册服务列表");
                    RpcResponseEntity rpcResponseEntity = new RpcResponseEntity(RegisterCenter.getServiceList());
                    SerializeUtil.send(rpcResponseEntity, socket);
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
