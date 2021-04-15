package com.zj.base.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;

import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;

/**
 * @author zj
 * @date 2020/12/30 10:57
 * @description 服务应用信息
 */
@Data
public class ServerInfo implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    String name;//服务名称
    String addr;//地址，ip，或者域名
    Integer port;//端口
    public ServerInfo() {
    }

    public ServerInfo(String name, String addr, Integer port) {
        this.name = name;
        this.addr = addr;
        this.port = port;
    }



    @Override
    public int hashCode() {
        return Objects.hash(name, addr, port);
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", port=" + port +
                '}';
    }
}