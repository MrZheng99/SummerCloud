package com.zj.rpc.monitor;

import com.esotericsoftware.kryo.KryoException;
import com.zj.base.constants.RegisterCenter;
import com.zj.base.constants.SocketCenter;
import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.base.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Monitor {
    private final Lock lock = new ReentrantLock();
    private final Condition run = lock.newCondition();
    private final Condition stop = lock.newCondition();
    private final Map<String, List<Socket>> socketList = new HashMap<>(1 << 3);

    /**
     * 如果没有服务在线，那么就暂停检测，等到有服务时再继续检测
     */
    public void start() {
        log.info("定时检测开启");
        Thread threadStop = new Thread(new StopThread());
        Thread threadRun = new Thread(new RunThread());
        threadRun.start();
        threadStop.start();
    }

    private class StopThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    log.warn("开始检测阻塞");
                    while (RegisterCenter.getServiceList().size() <= 0) {
                        System.out.println("等待服务中...5s");
                        TimeUnit.SECONDS.sleep(5);
                    }
                    run.signal();
                    log.warn("暂停检测阻塞");
                    stop.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();

                }
            }
        }
    }

    private class RunThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    log.warn("开始检测");
                    timeTaskRun();
                    log.info("暂停检测");
                    stop.signal();
                    run.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();

                }
            }
        }
    }

    private void timeTaskRun() throws InterruptedException {
        while (true) {
            Set<String> serviceSet = RegisterCenter.getRuList().keySet();
            Set<String> socketSet = socketList.keySet();
            log.info("socketSet【{}】,RegisterCenterServiceList::【{}】", socketSet,
                    RegisterCenter.getRuList());
            if (socketSet.size() > serviceSet.size()) {
                ArrayList<Object> objects = new ArrayList<>();
                socketSet.forEach((key -> {
                    if (!serviceSet.contains(key)) {
                        socketList.get(key).forEach((socket -> {
                            if (!socket.isClosed()) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                        objects.add(key);
                    }
                }));
                socketList.remove(objects);
            } else {
                serviceSet.forEach((key -> {
                    ConcurrentLinkedDeque<ServerInfo> serverInfos =
                            RegisterCenter.getServiceList().getOrDefault(key, null);
                    if (Objects.isNull(serverInfos))
                        return;
                    if (!socketSet.contains(key) || serverInfos.size() > 0) {
                        List<Socket> sockets = socketList.getOrDefault(key, new ArrayList<>());
                        for (ServerInfo serverInfo : serverInfos) {
                            try {
                                Socket socket = new Socket(serverInfo.getAddr(), serverInfo.getPort());
                                socket.setKeepAlive(true);
                                sockets.add(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        socketList.put(key, sockets);
                    }
                    RegisterCenter.getServiceList().remove(key);
                }));
            }
            if (socketList.size() > 0)
                check(socketList);
            else {
                log.info("暂无服务注册");
                return;
            }
            TimeUnit.SECONDS.sleep(6);
        }
    }

    //发请求检测服务是否存活
    public void check(Map<String, List<Socket>> socketList) {
        Map<String, List<Socket>> errorList = new HashMap<>();
        //log.info("socketList:{}", socketList);
        socketList.forEach((serviceName, sockets) -> {
            sockets.forEach(socket -> {
                try {
                    RpcRequestEntity rpcRequestEntity = new RpcRequestEntity(DataType.CHECK, null);
                    RpcResponseEntity rpcResponseEntity = SerializeUtil.sendAndAccept(RpcResponseEntity.class, rpcRequestEntity, socket);
//                    log.info("服务【{}】正常,socket【{}】,状态关闭【{}】,已连接【{}】", serviceName, socket, socket.isClosed(),
//                            socket.isConnected());
                    if (socket.isClosed() || !"SUCCESS".equals(String.valueOf(rpcResponseEntity.getData()))) {
                        log.error("未收到服务【{}】的回复,socket【{}】", serviceName, socket);
                        add(errorList, serviceName, socket);
                    }
                } catch (Exception e) {
                    if (e instanceof KryoException) {
                        add(errorList, serviceName, socket);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        });
        errorList.forEach((serviceName, list) -> {
            list.forEach(socket -> {
                log.info("删除serviceName【{}】，socket【{}】", serviceName, socket);
            });
            remove(SocketCenter.SEND_SOCKET_MAP, serviceName, list);
            RegisterCenter.remove(serviceName, list);
            remove(socketList, serviceName, list);
        });

    }

    public void remove(Map<String, List<Socket>> map, String key, List<Socket> vals) {
        List<Socket> o = map.get(key);
        if (Objects.isNull(o))
            return;
        if (o.size() == 1)
            map.remove(key);
        else {
            o.removeAll(vals);
            map.put(key, o);
        }

    }

    public void add(Map<String, List<Socket>> map, String key, Socket socket) {
        List<Socket> orDefault = map.getOrDefault(key, new ArrayList<>());
        orDefault.add(socket);
        map.put(key, orDefault);
    }

    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        monitor.start();
    }
}
