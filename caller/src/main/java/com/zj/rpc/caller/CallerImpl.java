package com.zj.rpc.caller;

import com.zj.zrpc.util.ZRpcTemplate;

/**
 * @author zj
 * @date 2021/1/7 15:25
 */
public class CallerImpl {
    public Object caller(int a,int b){
        ZRpcTemplate summerTemplate=new ZRpcTemplate();
        Object o = summerTemplate.get(10001,"callee", "com.zj.rpc.callee.CalculateImpl", "calculate", new Object[]{a, b});
        System.out.println(o);
        return o;
    }
}
