package com.zj.zrpc.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.*;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

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
    public void invoke(RpcRequestEntity rpcRequestEntity) throws IOException {
        log.info("INVOKE--->rpcRequestEntity: "+rpcRequestEntity.toString());
        InvokeData invokeData = (InvokeData) rpcRequestEntity.getData();
        //每个服务都有自己的SocketCenter
        Socket sk = SocketCenter.getByRequestID(rpcRequestEntity.getRequestID());
        String qualifiedName = invokeData.getQualifiedName();
        Object[] params = invokeData.getParams();
        String methodName = invokeData.getMethodName();
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
            Object o = clazz.newInstance();
            log.info("开始执行");
            Object rs = method.invoke(o,params);
            log.info("执行结果：{}",rs);
            SerializeUtil.send(new RpcResponseEntity(rs),sk);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
