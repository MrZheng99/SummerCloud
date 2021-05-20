package com.zj.base.entity;



import lombok.Data;

import java.io.Serializable;

/**
 * @author zj
 * @date 2020/12/30 10:57
 * @description 服务应用信息
 */
@Data
public class ServerInfo implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String name;//服务名称
    private String addr;//地址，ip，或者域名
    private Integer port;//端口
    private  String user;//安全校验用户名
    private  String password;//安全校验密码
    public ServerInfo() {
    }
    public ServerInfo(String addr, int port) {
        this.addr=addr;
        this.port=port;
    }
    public ServerInfo(String name, String addr, int port) {
        this.name=name;
        this.addr=addr;
        this.port=port;

    }
    public ServerInfo(String name, String addr, int port,String user,String password) {
        this.name=name;
        this.addr=addr;
        this.port=port;
        this.user=user;
        this.password=password;
    }
}