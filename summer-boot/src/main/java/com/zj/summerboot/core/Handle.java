package com.zj.summerboot.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.ServerInfo;
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
public class Handle implements Runnable {
    private final Socket socket;

    public Handle(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            RpcRequestEntity rpcRequestEntity = (RpcRequestEntity) ois.readObject();
            log.info(rpcRequestEntity.toString());
            switch (rpcRequestEntity.getDataType()){
                case REGISTER:
                    ServerInfo serverInfo = (ServerInfo) rpcRequestEntity.getData();
                    final RegisterCenter registerCenter = SummerBoot.getRegisterCenter();
                    registerCenter.add(serverInfo.getName(),serverInfo);
                    log.info("已有服务[{}]", registerCenter.getServiceList());
                    //TODO需要发给每个已经注册的服务做备份
                    distribute(registerCenter);
                    ois.close();
                    is.close();
                    break;
                case DISTRIBUTE:
                    RegisterCenter rc = (RegisterCenter) rpcRequestEntity.getData();
                    SummerBoot.setRegisterCenter(rc);
                    ois.close();
                    is.close();
                    break;
                case SEND:
                    Invoke invokeSend = new Invoke();
                    log.info("转发远程调用到具体服务器");
                    SocketCenter.add(rpcRequestEntity.getRequestID(),socket);
                    invokeSend.send(rpcRequestEntity);
                    log.info("转发远程调用到具体服务器结束");
                    break;
                case INVOKE:
                    log.info("处理远程调用");
                    Invoke invoke = new Invoke();
                    invoke.invoke(rpcRequestEntity);
                    log.info("处理远程调用结束");
                    ois.close();
                    is.close();
                    break;
                default:
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    private void distribute(RegisterCenter registerCenter) throws IOException {
        Socket sk=null;
        Iterator<Map.Entry<String, ServerInfo>> it = registerCenter.getServiceList().entrySet().iterator();
        Map.Entry<String, ServerInfo> entry=null;
        ServerInfo serverInfo=null;
        while (it.hasNext()) {
            entry= it.next();
            serverInfo=entry.getValue();
            sk=new Socket(serverInfo.getAddr(),serverInfo.getPort());
            OutputStream outputStream = sk.getOutputStream();
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new RpcRequestEntity(DataType.DISTRIBUTE,registerCenter));
            objectOutputStream.flush();
            outputStream.flush();
            objectOutputStream.close();
            outputStream.close();
            sk.close();
        }
    }
}
