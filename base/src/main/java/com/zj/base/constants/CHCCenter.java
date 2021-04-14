package com.zj.base.constants;

import io.netty.channel.ChannelHandlerContext;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zj
 * @date 2021/1/6 9:40
 */
public class CHCCenter {
    private static ConcurrentMap<Long, ChannelHandlerContext> SOCKET_LIST=new ConcurrentHashMap<>();
    public static ChannelHandlerContext getByRequestID(Long requestId){
        return SOCKET_LIST.getOrDefault(requestId,null);
    }
    public static void removeByRequestID(Long requestId){
        SOCKET_LIST.remove(requestId);
    }
    public static void add(Long requestId,ChannelHandlerContext socket){
        SOCKET_LIST.put(requestId,socket);
    }
}
