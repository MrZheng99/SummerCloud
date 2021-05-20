package com.zj.rpc.caller;

import com.zj.service.util.ZRpcTemplate;

/**
 * @author zj
 * @date 2021/1/7 15:25
 */
public class CallerImpl {
    public Object caller(int a,int b){
        ZRpcTemplate summerTemplate=new ZRpcTemplate();
        Object o = summerTemplate.get("127.0.0.1",10000,"callee", "com.zj.rpc.callee.CalculateImpl", "calculate",
          new Object[]{a, b});
        return o;
    }
}
//public class CallerImpl {
//    public Object caller(int a,int b){
//        ZRpcTemplate summerTemplate=new ZRpcTemplate();
//        System.out.println("执行测试的了");
//        Object o = summerTemplate.get("127.0.0.1",10000,"callee", "com.zj.rpc.callee.CalculateImpl", "calculate",
//                new Object[]{a, b});
//        return o;
//    }
//}
