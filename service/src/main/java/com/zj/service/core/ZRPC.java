package com.zj.service.core;

import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServiceConfigDefinition;
import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.base.util.SerializeUtil;
import com.zj.register.core.Register;
import com.zj.register.core.RegisterConfigDefinition;
import com.zj.register.core.RegisterHandle;
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
    public static void run(Class<?> clazz) throws IOException, RegisterPasswordCheckFail {
        ServiceConfigDefinition serviceSCD = ServiceConfigDefinition.readConfig(clazz, "application.properties", "zj");
        RegisterConfigDefinition registerRCD = RegisterConfigDefinition.readConfig(clazz, "application.properties",
                "zj.register");
        final String addr = serviceSCD.getAddr();
        final int port = serviceSCD.getPort();
        final String name = serviceSCD.getName();
        if(!registerRCD.isIdentifyEnable()){
            Socket sk = new Socket(registerRCD.getAddr(),registerRCD.getPort());
            serviceSCD.setUser(registerRCD.getUser());
            serviceSCD.setPassword(registerRCD.getPassword());
            Boolean checkPass = (Boolean) SerializeUtil.sendAndAccept(RpcResponseEntity.class,
                    new RpcRequestEntity(DataType.CHECK_PASSWORD,
                    serviceSCD), sk,true).getData();
            if(!checkPass){
                throw  new RegisterPasswordCheckFail("账户:"+serviceSCD.getUser()+"或 密码:"+serviceSCD.getPassword()+"不正确");
            }
        }
        Register.register(registerRCD,serviceSCD);
        ServerSocket ssk = new ServerSocket();
        ssk.bind(new InetSocketAddress(addr, port));
        log.info("启动服务-{} 成功，占用地址：{}，端口：{}", name, addr, port);
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
