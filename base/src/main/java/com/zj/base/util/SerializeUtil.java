package com.zj.base.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zj.base.entity.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列化工具
 */
@Slf4j
public class SerializeUtil {
    private static final Kryo kryo = new Kryo();

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
     * @param obj          序列化的对象
     * @param socket 输出流
     */
    public static void send(Object obj, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, obj);
        output.flush();
       // log.info("已经发送");

    }

    public static void send(Object obj, Socket socket, Boolean closed) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        Output output = new Output(outputStream);

        kryo.writeObject(output, obj);
        output.flush();
        if (closed) {
            output.close();
        }
    }

    /**
     * @param type   返回指定的类型
     * @param socket socket
     * @param <T>    泛型
     * @return 返回输入流中的结果
     */
    public static <T> T accept(Class<T> type, Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        Input input = new Input(inputStream);
       // log.info("开始读取数据");
        T t = kryo.readObject(input, type);
       // log.info("读取数据完毕");
        return t;
    }

    public static <T> T accept(Class<T> type, Socket socket, Boolean closed) throws IOException {
        InputStream inputStream = socket.getInputStream();
        Input input = new Input(inputStream);
        //log.info("开始读取数据");
        T t = kryo.readObject(input, type);
        //log.info("读取数据完毕");
        if (closed) {
            input.close();
        }
        return t;
    }
}
