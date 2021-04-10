package com.zj.base.entity;

import java.io.Serializable;

/**
 * @author zj
 * @date 2020/12/31 11:14
 */
public class RpcResponseEntity implements Serializable {
    private static final long serialVersionUID = -9169317700038917792L;
    private Long requestID;
    private Object data;

    public RpcResponseEntity() {
        this.requestID=System.currentTimeMillis();
        this.data = null;
    }
    public RpcResponseEntity(Object data) {
        this.requestID=System.currentTimeMillis();
        this.data = data;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
