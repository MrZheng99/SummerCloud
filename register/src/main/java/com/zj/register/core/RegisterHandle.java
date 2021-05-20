package com.zj.register.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.base.util.SerializeUtil;
import com.zj.register.conf.RegisterConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.stream.Collectors;

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
                    log.info("服务在线列表");
                    RegisterCenter.getRuList().forEach((k,v)->{
                        log.info("服务名称【{}】",k);
                           v.forEach(e->{
                               log.info("addr="+e.getAddr()+",port="+e.getPort()+"\t");
                           });

                    });
                    is.close();
                    break;
                case SEND:
                    SocketCenter.add(rpcRequestEntity.getRequestID(), socket);
                    log.info("转发远程调用到具体服务器");
                    Send invokeSend = new Send();
                    invokeSend.send(rpcRequestEntity);
                    log.info("转发远程调用到具体服务器结束");
                    break;
                case CHECK_PASSWORD:
                    ServiceConfigDefinition si = (ServiceConfigDefinition) rpcRequestEntity.getData();
                    RpcResponseEntity   rr = new RpcResponseEntity(true);
                    if(RegisterConfig.CONF.isIdentifyEnable()){
                        if(!RegisterConfig.CONF.getUser().equals(si.getUser())||!RegisterConfig.CONF.getPassword().equals(si.getPassword())){
                            log.error("服务【{}】尝试连接到注册中心失败",si.getName());
                            rr = new RpcResponseEntity(false);
                        }
                    }
                    SerializeUtil.send(rr, socket);
                    break;
                case GET_SERVICE_LIST:
                    log.info("获取已注册服务列表");
                    RpcResponseEntity rpcResponseEntity = new RpcResponseEntity(RegisterCenter.getRuList());
                    SerializeUtil.send(rpcResponseEntity, socket);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
