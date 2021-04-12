package com.zj.zrpc.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zj
 * @date 2020/12/31 12:10
 * @description 处理请求
 */
@Slf4j
public class ServerHandle implements Runnable {
    private final Socket socket;

    public ServerHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                log.info("{}", socket);
                RpcRequestEntity rpcRequestEntity = SerializeUtil.accept(RpcRequestEntity.class, socket);
                switch (rpcRequestEntity.getDataType()) {
                    case CHECK:
                        log.info("服务状态检测");
                        SerializeUtil.send(new RpcResponseEntity("SUCCESS"), socket);
                        break;
                    case INVOKE:
                        SocketCenter.add(rpcRequestEntity.getRequestID(), socket);
                        log.info("处理远程调用");
                        Invoke invokeMethod = new Invoke();
                        invokeMethod.invoke(rpcRequestEntity);
                        log.info("处理远程调用结束");
                        break;
                    default:
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
