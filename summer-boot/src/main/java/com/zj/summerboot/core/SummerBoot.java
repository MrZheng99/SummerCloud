package com.zj.summerboot.core;

import com.zj.base.constants.RegisterCenter;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.register.core.Register;
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
public class SummerBoot {
    private static RegisterCenter registerCenter=new RegisterCenter();
    public static RegisterCenter getRegisterCenter(){
        return registerCenter;
    }
    public static void setRegisterCenter(RegisterCenter rc){
       registerCenter=rc;
    }
    public static void run(Class<?> clazz) throws IOException {
        ServiceConfigDefinition serviceSCD = ServiceConfigDefinition.readConfig(clazz, "application.properties", "zj");
        final String addr = serviceSCD.getAddr();
        final int port = serviceSCD.getPort();
        final String name = serviceSCD.getName();
        ServerSocket ssk = new ServerSocket();
        ssk.bind(new InetSocketAddress(addr, port));
        log.info("启动服务-{} 成功，占用地址：{}，端口：{}", name, addr, port);
        Register register = new Register(serviceSCD);
        register.register(clazz,"application.properties", "zj.register");
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                corePoolSize * 3, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(corePoolSize * 2),
                new ThreadPoolExecutor.AbortPolicy());
        while (true) {
            Socket socket = ssk.accept();
            log.info(socket.toString());
            threadPoolExecutor.submit(new Handle(socket));
        }
    }
}
