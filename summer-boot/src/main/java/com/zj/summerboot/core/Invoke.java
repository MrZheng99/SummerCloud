package com.zj.summerboot.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.register.core.Register;
import com.zj.summerboot.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

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
        ServerInfo serverInfo = SummerBoot.getRegisterCenter().getByName(serviceName);
        Socket sk = new Socket(serverInfo.getAddr(),serverInfo.getPort());
        log.warn("send--->>>>>{}",sk);
        OutputStream outputStream = sk.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        rpcRequestEntity.setDataType(DataType.INVOKE);
        oos.writeObject(rpcRequestEntity);
        log.info("send----{}",rpcRequestEntity);
        outputStream.flush();
        oos.flush();
        oos.close();
        outputStream.close();
        sk.close();
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
            log.info(String.valueOf(rs));
            OutputStream os = sk.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(new RpcResponseEntity(rs));
            oos.flush();
            System.out.println(sk.getLocalPort());
            os.flush();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }


    }
}
