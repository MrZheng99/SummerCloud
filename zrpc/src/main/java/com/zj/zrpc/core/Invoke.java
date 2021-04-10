package com.zj.zrpc.core;

import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.*;
import com.zj.base.util.SerializeUtil;
import com.zj.zrpc.entity.RegisterConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

/**
 * @author zj
 * @date 2020/12/30 17:18
 */
@Slf4j
public class Invoke {
    public void invoke(InvokeData invokeData){
    }

    public void back(ReturnData returnData) {

    }
    public void send(RpcRequestEntity rpcRequestEntity) throws IOException {
        InvokeData invokeData = (InvokeData) rpcRequestEntity.getData();
        String serviceName = invokeData.getServiceName();
        //转发请求到具体服务器执行
//        ServerInfo serverInfo = ZRPC.getRegisterCenter().getByName(serviceName);
        Socket register_sk = new Socket(RegisterConfig.CONF.getAddr(),RegisterConfig.CONF.getPort());
        SerializeUtil.send(new RpcRequestEntity(DataType.GET_SERVICE,new ServerInfo(serviceName,null,null)),
                register_sk.getOutputStream());
        RpcResponseEntity rpcResponseEntity = SerializeUtil.accept(RpcResponseEntity.class, register_sk.getInputStream(),true);
        ServerInfo serverInfo = (ServerInfo) rpcResponseEntity.getData();
        log.info("serverInfo:{}",serverInfo);
        Socket sk = new Socket(serverInfo.getAddr(),serverInfo.getPort());
        OutputStream outputStream = sk.getOutputStream();
        //todo 序列化
        SerializeUtil.send(rpcRequestEntity,outputStream);
    }
    public void invoke(RpcRequestEntity rpcRequestEntity) throws IOException {
        log.info("invoke----{}",rpcRequestEntity);
        InvokeData invokeData = (InvokeData) rpcRequestEntity.getData();
        Socket sk = SocketCenter.getByRequestID(rpcRequestEntity.getRequestID());
        String qualifiedName = invokeData.getQualifiedName();
        Object[] params = invokeData.getParams();
        String methodName = invokeData.getMethodName();
        //Class<?>[] parameterTypes = invokeData.getParameterTypes();
        //具体执行方法
        try {
            Class<?> clazz = Class.forName(qualifiedName);
            Method method = null;
            for (Method me : clazz.getMethods()) {
               if (methodName.equals(me.getName())){
                   method=me;
                   break;
               }
            }
            if (method==null) {
                throw new NoSuchMethodException("没有方法："+methodName);
            }
            //Method method = clazz.getDeclaredMethod(methodName,parameterTypes);
            Object o = clazz.newInstance();
            Object rs = method.invoke(o,params);
            OutputStream os = sk.getOutputStream();
            //todo 序列化
            SerializeUtil.send(new RpcResponseEntity(rs),os);

        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }


    }
}
