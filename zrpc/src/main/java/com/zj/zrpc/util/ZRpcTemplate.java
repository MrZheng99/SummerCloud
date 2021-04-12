package com.zj.zrpc.util;

import com.zj.base.entity.*;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * @author zj
 * 2021/1/7 15:07
 */
@Slf4j
public class ZRpcTemplate {
    public  Object get(String host,Integer port,String serviceName,String qualifiedName,String methodName,
                      Object[] params){
        Object rs=null;
        try {
            Socket sk = new Socket(host,port);
            sk.setKeepAlive(true);
            InvokeData invokeData = new InvokeData();
            invokeData.setServiceName(serviceName);
            invokeData.setQualifiedName(qualifiedName);
            invokeData.setMethodName(methodName);
            invokeData.setParams(params);
            RpcRequestEntity rpcRequestEntity = new RpcRequestEntity(DataType.SEND, invokeData);
            SerializeUtil.send(rpcRequestEntity,sk);
            RpcResponseEntity rpcResponseEntity=SerializeUtil.accept(RpcResponseEntity.class,sk,true);
            rs =  rpcResponseEntity.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

}
