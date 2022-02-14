package com.zkyouxi.zhangyucheng.dynamicProxyTesting;

import java.lang.reflect.Proxy;

public class Testing {
    public static void main(String[] args) {
        RealSubjectA rsA = new RealSubjectA();
        SubjectA proxySubjectA = (SubjectA) Proxy.newProxyInstance(SubjectA.class.getClassLoader()
                , new Class[]{SubjectA.class}, new LogHandler(rsA));

        RealSubjectB rsB = new RealSubjectB();
        SubjectB proxySubjectB = (SubjectB) Proxy.newProxyInstance(SubjectB.class.getClassLoader()
                , new Class[]{SubjectB.class}, new LogHandler(rsB));

        proxySubjectA.setUser("Byron","123456");
        proxySubjectB.sayHello("Byron");

        
    }
}



