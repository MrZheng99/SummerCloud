package com.zj.base.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author zj
 * @date 2020/12/31 10:51
 * @description 调用方法需要的数据
 */
public class InvokeData implements Serializable {
    private static final long serialVersionUID = 1249279357115097846L;
    private String serviceName;//服务名
    private String qualifiedName;//类的全限定名
    private String methodName;//方法名
    private Class<?>[] parameterTypes;//参数类别
    private Object[] params;//参数

    public InvokeData(String serviceName, String qualifiedName, String methodName, Class<?>[] parameterTypes, Object[] params) {
        this.serviceName = serviceName;
        this.qualifiedName = qualifiedName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.params = params;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public InvokeData() {
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "InvokeData{" +
                "serviceName='" + serviceName + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
