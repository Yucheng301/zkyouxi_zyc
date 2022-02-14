package com.zkyouxi.zhangyucheng.proxytesting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler{
    private Object realObject;


    public Object proxyInstance(Object realObject){
        this.realObject = realObject;
        return Proxy.newProxyInstance(realObject.getClass().getClassLoader(), realObject.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(realObject,args);
        return result;
    }
}
