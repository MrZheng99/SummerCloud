package com.zj.base.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zj.base.entity.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列化工具
 */
public class SerializeUtil {
    private static final Kryo kryo=new Kryo();
    static {
        kryo.register(Integer.class);
        kryo.register(Long.class);
        kryo.register(DataType.class);
        kryo.register(InvokeData.class);
        kryo.register(ServerInfo.class);
        kryo.register(ReturnData.class);
        kryo.register(AtomicLong.class);
        kryo.register(RpcRequestEntity.class);
        kryo.register(RpcResponseEntity.class);
        kryo.register(Class[].class);
        kryo.register(Object[].class);

    }
    /**
     *
     * @param obj 序列化的对象
     * @param outputStream 输出流
     */
    public static void send(Object obj, OutputStream outputStream){
            Output output = new Output(outputStream);
            kryo.writeObject(output, obj);
            output.flush();
    }
    public static void send(Object obj, OutputStream outputStream,Boolean closed){
            Output output = new Output(outputStream);
            kryo.writeObject(output, obj);
            output.flush();
            if(closed){
                output.close();
            }
    }
    /**
     *
     * @param type 返回指定的类型
     * @param inputStream 输入流
     * @param <T> 泛型
     * @return 返回输入流中的结果
     */
    public static <T> T accept(Class<T> type, InputStream inputStream){
            Input input = new Input(inputStream);
            System.out.println("开始读取数据");
            T t= kryo.readObject(input,type);
            System.out.println("读取数据完毕");
            return t;
    }
    public static <T> T accept(Class<T> type, InputStream inputStream,Boolean closed){
        Input input = new Input(inputStream);
        System.out.println("开始读取数据");
        T t = kryo.readObject(input,type);
        System.out.println("读取数据完毕");
        if(closed){
            input.close();
        }
        return t;
    }
}
