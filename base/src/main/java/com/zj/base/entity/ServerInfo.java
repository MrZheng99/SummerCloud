package com.zj.base.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zj
 * @date 2020/12/30 10:57
 * @description 服务应用信息
 */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerInfo that = (ServerInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(addr, that.addr) &&
                Objects.equals(port, that.port);
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