package com.zj.base.entity;

import java.io.Serializable;

public class RpcRequestEntity implements Serializable {
    private static final long serialVersionUID = 788444142107735152L;
    private  Long requestID;
    private DataType dataType;
    private Object data;
    public RpcRequestEntity() {
        this.requestID=System.currentTimeMillis();
    }
    public RpcRequestEntity(DataType dataType, Object data) {
        this.requestID=System.currentTimeMillis();
        this.dataType = dataType;
        this.data = data;
    }

    public Long getRequestID() {
        return requestID;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



    @Override
    public String toString() {
        return "RpcRequestEntity{" +
                "requestID=" + requestID +
                ", dataType=" + dataType +
                ", data=" + data +
                '}';
    }

}
