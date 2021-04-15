package com.zj.base.exception;

public class NotFoundLoadBalanceType extends RuntimeException{
    private static final long serialVersionUID = 2607017095070081134L;
    public NotFoundLoadBalanceType(String msg){
        super(msg);
    }
}
