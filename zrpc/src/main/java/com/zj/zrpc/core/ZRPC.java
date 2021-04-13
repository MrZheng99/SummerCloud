package com.zj.zrpc.core;

import com.esotericsoftware.kryo.Kryo;
import com.zj.base.constants.RegisterCenter;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.util.KryoDecoder;
import com.zj.base.util.KryoEncoder;
import com.zj.register.core.Register;
import com.zj.register.core.RegisterConfigDefinition;
import com.zj.register.core.RegisterHandle;
import com.zj.zrpc.entity.RegisterConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
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

    public static void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
//                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//                pipeline.addLast(new LengthFieldPrepender(4));
                pipeline.addLast("encoder", new KryoEncoder());
                pipeline.addLast("decoder", new KryoDecoder());
                pipeline.addLast(new RegisterTestHandle());
            }
        });
        System.out.println("服务端准备就绪，随时可以起飞~");
        //连接服务端
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind("127.0.0.1", 6666).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        start();
    }
}
