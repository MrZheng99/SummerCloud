package com.zj.rpc.monitor;

import com.esotericsoftware.kryo.KryoException;
import com.zj.base.constants.RegisterCenter;
import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

@Slf4j
public class Monitor {
    public void start() {
        Timer timer = new Timer();
        log.info("定时检测开启");
        Map<String, Socket> socketList = new HashMap<>(1 << 3);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (Monitor.class) {
                    Set<String> serviceSet = RegisterCenter.getServiceList().keySet();
                    Set<String> socketSet = socketList.keySet();
                    log.info("socketSet【{}】,RegisterCenterServiceList::【{}】", socketSet,
                            RegisterCenter.getServiceList());
//                    log.info("socketSet::size【{}】,serviceSet::size【{}】",socketSet.size(),serviceSet.size());
                    if (socketSet.size() > serviceSet.size()) {
                        socketSet.forEach((key -> {
                            if (!serviceSet.contains(key)) {
                                if (!socketList.get(key).isClosed()) {
                                    try {
                                        socketList.get(key).close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                socketList.remove(key);
                            }
                        }));
                    } else if (socketSet.size() < serviceSet.size()) {
                        serviceSet.forEach((key -> {
                            if (!socketSet.contains(key)) {
                                ServerInfo serverInfo = RegisterCenter.getServiceList().get(key);
                                try {
                                    Socket socket = new Socket(serverInfo.getAddr(), serverInfo.getPort());
                                    socket.setKeepAlive(true);
                                    socketList.put(key, socket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                    }
                    if (serviceSet.size() > 0)
                        check(socketList);
                    else
                        log.info("暂无服务注册");
                }
            }
        }, 1000, 10000);
    }

    //发请求检测服务是否存活
    public void check(Map<String, Socket> socketList) {
        List<String> errorList = new ArrayList<>();
        socketList.forEach((serviceName, socket) -> {
            try {
                RpcRequestEntity rpcRequestEntity = new RpcRequestEntity(DataType.CHECK, null);
                log.info("socket【{}】,状态关闭【{}】,已连接【{}】", socket, socket.isClosed(), socket.isConnected());
                RpcResponseEntity rpcResponseEntity = SerializeUtil.sendAndAccept(RpcResponseEntity.class, rpcRequestEntity, socket);
                if (socket.isClosed() || !"SUCCESS".equals(String.valueOf(rpcResponseEntity.getData()))) {
                    log.error("未收到服务【{}】的回复", serviceName);
                    errorList.add(serviceName);
                } else {
                    log.info("服务【{}】正常", serviceName);
                }
            } catch (Exception e) {
                if (e instanceof KryoException ) {
                    errorList.add(serviceName);
                    log.error("服务【{}】已关闭", serviceName);
                } else {
                    e.printStackTrace();
                }
            }
        });
        errorList.forEach((key -> {
            log.info("删除socket【{}】", key);
            RegisterCenter.removeByName(key);
            socketList.remove(key);
        }));

    }

    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        monitor.start();
    }
}
