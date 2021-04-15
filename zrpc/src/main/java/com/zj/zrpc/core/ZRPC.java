package com.zj.zrpc.core;

import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.util.KryoDecoder;
import com.zj.base.util.KryoEncoder;
import com.zj.register.core.Register;
import com.zj.register.core.RegisterConfigDefinition;
import com.zj.register.core.RegisterHandle;
import com.zj.register.conf.RegisterConfig;
import com.zj.zrpc.netty.RegisterTestHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zj
 * @date 2020/12/30 15:04
 */
@Slf4j
public class ZRPC {
    public static void run(Class<?> clazz) throws IOException {
        ServiceConfigDefinition serviceSCD = ServiceConfigDefinition.readConfig(clazz, "application.properties", "zj");
        final String addr = serviceSCD.getAddr();
        final int port = serviceSCD.getPort();
        final String name = serviceSCD.getName();
        ServerSocket ssk = new ServerSocket();
        ssk.bind(new InetSocketAddress(addr, port));
        log.info("启动服务-{} 成功，占用地址：{}，端口：{}", name, addr, port);
        Register register = new Register(serviceSCD);
        RegisterConfigDefinition registerRCD = RegisterConfigDefinition.readConfig(clazz, "application.properties",
                "zj.register");
        register.register(registerRCD);
        RegisterConfig.CONF = registerRCD;
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                corePoolSize * 3, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(corePoolSize * 2),
                new ThreadPoolExecutor.AbortPolicy());
        if (registerRCD.isIdentifyEnable()) {
            while (true) {
                Socket socket = ssk.accept();
                threadPoolExecutor.submit(new RegisterHandle(socket));
            }
        } else {
            while (true) {
                Socket socket = ssk.accept();
                threadPoolExecutor.submit(new ServerHandle(socket));
            }
        }
    }


}
