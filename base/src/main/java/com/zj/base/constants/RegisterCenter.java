package com.zj.base.constants;

import com.zj.base.entity.LoadBalanceType;
import com.zj.base.entity.ServerInfo;

import java.io.Serializable;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * @author zj
 * @date 2020/12/30 16:28
 * @description 注册服务的管理
 */

public class RegisterCenter implements Serializable {
    /**
     * 提供负载均衡策略
     * 1.随机
     * 2.按顺序
     * 3.最近未使用
     */
    private static final long serialVersionUID = -1361117368916951498L;
    private static final Map<String, ConcurrentLinkedDeque<ServerInfo>> SERVICE_LIST = new  ConcurrentHashMap<>();
    private static final Map<String, ConcurrentLinkedDeque<ServerInfo>> RU_LIST = new ConcurrentHashMap<String, ConcurrentLinkedDeque<ServerInfo>>();
    public static  Map<String, ConcurrentLinkedDeque<ServerInfo>> getRuList() {
        return RU_LIST;
    }
    public static  Map<String, ConcurrentLinkedDeque<ServerInfo>> getServiceList() {
        return SERVICE_LIST;
    }
    public static ServerInfo getByName(LoadBalanceType type, String name) {
        switch (type) {
            case RU:
                return getRU(name);
            case POLL:
                return getPoll(name);
            case RANDOM:
                return getRandom(name);
            default:
                return null;
        }
    }

    public static void add(String name, ServerInfo serverInfo) {
        ConcurrentLinkedDeque<ServerInfo> orDefault = RU_LIST.getOrDefault(name, null);
        if (Objects.isNull(orDefault)) {
            orDefault = new ConcurrentLinkedDeque<>();
        }
        orDefault.addLast(serverInfo);
        RU_LIST.put(name, orDefault);
        ConcurrentLinkedDeque<ServerInfo> o = SERVICE_LIST.getOrDefault(name, null);
        if (Objects.isNull(o)) {
            o = new ConcurrentLinkedDeque<>();
        }
        o.addLast(serverInfo);
        SERVICE_LIST.put(name, o);    }

    public static void removeByName( ServerInfo serverInfo) {
        String name = serverInfo.getName();
        ConcurrentLinkedDeque<ServerInfo> orDefault = RU_LIST.getOrDefault(name, null);
        if (!Objects.isNull(orDefault)) {
            int size = orDefault.size();
            if (size == 1)
                RU_LIST.remove(name);
            else
                orDefault.remove(serverInfo);
        }
    }
    public static void remove(String name,List<Socket> sockets) {
        List<ServerInfo> serverInfos = new ArrayList<>();
        sockets.forEach(socket -> {
            serverInfos.add(new ServerInfo(name,socket.getInetAddress().getHostAddress(),socket.getPort()));
        });
        ConcurrentLinkedDeque<ServerInfo> orDefault = RU_LIST.getOrDefault(name, null);
        if(!Objects.isNull(orDefault)){
            int size = orDefault.size();
            if (size == 1)
                RU_LIST.remove(name);
            else{
                orDefault.removeAll(serverInfos);
                RU_LIST.put(name,orDefault);
             }
        }

    }
    public static ServerInfo getRU(String name) {
        ConcurrentLinkedDeque<ServerInfo> serviceList = RU_LIST.getOrDefault(name, null);
        ServerInfo first = null;
        if (serviceList != null) {
            first = serviceList.getFirst();
            serviceList.removeFirst();
            serviceList.addLast(first);
        }
        return first;
    }

    public static ServerInfo getRandom(String name) {
        ConcurrentLinkedDeque<ServerInfo> serviceList = RU_LIST.getOrDefault(name, null);
        ServerInfo first = null;
        if (serviceList != null) {
            List<ServerInfo> list = new ArrayList<>(serviceList);
            int size = list.size();
            Random random = new Random();
            int i = random.nextInt(size);
            first = list.get(i);
        }
        return first;
    }

    public static ServerInfo getPoll(String name) {
        return getRU(name);
    }
}

