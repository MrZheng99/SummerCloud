package com.zj.base.constants;

import com.zj.base.entity.ServerInfo;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zj
 * @date 2021/1/6 9:40
 */
public class SocketCenter {
    private static ConcurrentMap<Long, Socket> SOCKET_LIST=new ConcurrentHashMap<>();
    public static Socket getByRequestID(Long requestId){
        return SOCKET_LIST.getOrDefault(requestId,null);
    }
    public static void removeByRequestID(Long requestId){
        SOCKET_LIST.remove(requestId);
    }
    public static void add(Long requestId,Socket socket){
        SOCKET_LIST.put(requestId,socket);
    }

    //缓存分发请求的socket
    public static Map<String,Socket> SEND_SOCKET_MAP=new HashMap<>();

}
