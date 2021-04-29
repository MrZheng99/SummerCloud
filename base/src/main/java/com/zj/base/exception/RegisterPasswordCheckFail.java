package com.zj.base.exception;

public class RegisterPasswordCheckFail extends RuntimeException{
    private static final long serialVersionUID = -3596172695700970344L;
    public RegisterPasswordCheckFail(String msg){
        super(msg);
    }
}
