package com.zj.base.constants;

import com.zj.base.entity.ServerInfo;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zj
 * @date 2021/1/6 9:40
 */
public class SocketCenter {
    private static ConcurrentMap<Long, Socket> SOCKET_LIST = new ConcurrentHashMap<>();

    public static Socket getByRequestID(Long requestId) {
        return SOCKET_LIST.getOrDefault(requestId, null);
    }

    public static void add(Long requestId, Socket socket) {
        SOCKET_LIST.put(requestId, socket);
    }

    //缓存分发请求的socket
    public static Map<String, List<Socket>> SEND_SOCKET_MAP = new HashMap<>();

    public static Socket get(ServerInfo serverInfo) {
        String name = serverInfo.getName();
        List<Socket> sockets = SEND_SOCKET_MAP.get(name);
        if (Objects.isNull(sockets))
            return null;
        for (Socket socket : sockets) {
            if (serverInfo.getPort().equals(socket.getPort()) && serverInfo.getAddr().equals(socket.getLocalAddress().getHostAddress())) {
                return socket;
            }
        }
        return null;
    }

    public static void add(String name, Socket socket) {
        List<Socket> sockets = SEND_SOCKET_MAP.getOrDefault(name, new ArrayList<>());
        sockets.add(socket);
        SEND_SOCKET_MAP.put(name, sockets);
    }
}
