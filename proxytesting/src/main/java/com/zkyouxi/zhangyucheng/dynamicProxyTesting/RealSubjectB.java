package com.zkyouxi.zhangyucheng.dynamicProxyTesting;

public class RealSubjectB implements SubjectB {
    @Override
    public void sayHello(String name) {
        System.out.println("--------------sayHello: " + name + " ----------------");
    }
}
