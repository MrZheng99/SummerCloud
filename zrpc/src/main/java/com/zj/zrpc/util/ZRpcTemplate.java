package com.zj.zrpc.util;

import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.util.SerializeUtil;
import com.zj.base.entity.InvokeData;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author zj
 * @date 2021/1/7 15:07
 */
public class ZRpcTemplate<T> {
    public Object get(Integer port,String serviceName,String qualifiedName,String methodName,Object[] params){
        Object rs=null;
        Socket sk=null;
        try {
            sk = new Socket("127.0.0.1",port );
            sk.setKeepAlive(true);
            OutputStream outputStream = sk.getOutputStream();
            InvokeData invokeData = new InvokeData();
            invokeData.setServiceName(serviceName);
            invokeData.setQualifiedName(qualifiedName);
            invokeData.setMethodName(methodName);
            invokeData.setParams(params);
            RpcRequestEntity rpcRequestEntity = new RpcRequestEntity(DataType.INVOKE, invokeData);
            SerializeUtil.send(rpcRequestEntity,outputStream,false);
            /**************/
            InputStream inputStream = sk.getInputStream();
            RpcResponseEntity rpcResponseEntity=SerializeUtil.accept(RpcResponseEntity.class,inputStream);
            rs =  rpcResponseEntity.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
