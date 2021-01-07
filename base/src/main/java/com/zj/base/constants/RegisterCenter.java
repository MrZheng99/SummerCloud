package com.zj.base.constants;
import com.zj.base.entity.ServerInfo;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zj
 * @date 2020/12/30 16:28
 * @description 注册服务的管理
 */

public class RegisterCenter implements Serializable {
    private static final long serialVersionUID = -1361117368916951498L;

    //    public static Map<String, ServerInfo> getServiceList() {
//        return SERVICE_LIST;
//    }
//
//    private static Map<String, ServerInfo> SERVICE_LIST = new ConcurrentHashMap<String,ServerInfo>();
//
//    public static ServerInfo getByName(String name){
//        return SERVICE_LIST.getOrDefault(name,null);
//    }
//    public static void add(String name,ServerInfo serverInfo){
//        SERVICE_LIST.put(name,serverInfo);
//    }
//    public static void removeByName(String name){
//        SERVICE_LIST.remove(name);
//    }
    public  Map<String, ServerInfo> getServiceList() {
        return SERVICE_LIST;
    }
    private  Map<String, ServerInfo> SERVICE_LIST = new ConcurrentHashMap<String,ServerInfo>();

    public  ServerInfo getByName(String name){
        return SERVICE_LIST.getOrDefault(name,null);
    }
    public  void add(String name,ServerInfo serverInfo){
        SERVICE_LIST.put(name,serverInfo);
    }
    public  void removeByName(String name){
        SERVICE_LIST.remove(name);
    }
}
