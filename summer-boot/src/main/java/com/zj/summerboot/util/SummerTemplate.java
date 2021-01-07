package com.zj.summerboot.util;

import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.summerboot.entity.InvokeData;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author zj
 * @date 2021/1/7 15:07
 */
public class SummerTemplate<T> {
    public Object get(Integer port,String serviceName,String qualifiedName,String methodName,Object[] params){
        Object rs=null;
        try {
            Socket sk = new Socket("127.0.0.1",port );
            sk.setKeepAlive(true);
            OutputStream outputStream = sk.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            InvokeData invokeData = new InvokeData();
            invokeData.setServiceName(serviceName);
            invokeData.setQualifiedName(qualifiedName);
            invokeData.setMethodName(methodName);
            invokeData.setParams(params);
            //invokeData.setParameterTypes(new Class[]{Integer.class, Integer.class});
            RpcRequestEntity rpcRequestEntity = new RpcRequestEntity(DataType.SEND, invokeData);
            oos.writeObject(rpcRequestEntity);
            oos.flush();
           // oos.close();
            /**************/
            InputStream inputStream = sk.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcResponseEntity rpcResponseEntity = (RpcResponseEntity) objectInputStream.readObject();
            rs =  rpcResponseEntity.getData();
            objectInputStream.close();
            inputStream.close();
            sk.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            return rs;
        }
    }
}
