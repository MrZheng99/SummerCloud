package com.zj.rpc.callee;

import com.zj.rpc.calleeapi.ICalculate;
import com.zj.rpc.calleeapi.MNumber;

public class CalculateImpl implements ICalculate {

    @Override
    public Integer calculate(int a,int b) {
        return a+b;
    }
}
