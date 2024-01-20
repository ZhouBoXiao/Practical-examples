package com.example.aviator;


import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class AddFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        long num1 = FunctionUtils.getNumberValue(arg1, env).longValue();
        long num2 = FunctionUtils.getNumberValue(arg2, env).longValue();
        return AviatorLong.valueOf(num1+num2);
    }

    @Override
    public String getName() {
        return "add";
    }
}