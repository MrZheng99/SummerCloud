package com.zj.rpc.callee;

import com.zj.summerboot.util.SummerTemplate;

/**
 * @author zj
 * @date 2021/1/7 15:25
 */
public class CallerImpl {
    public Object caller(int a,int b){
        SummerTemplate summerTemplate=new SummerTemplate();
        Object o = summerTemplate.get(10001,"callee", "com.zj.rpc.callee.CalculateImpl", "calculate", new Object[]{a, b});
        System.out.println(o);
        return o;
    }
}
