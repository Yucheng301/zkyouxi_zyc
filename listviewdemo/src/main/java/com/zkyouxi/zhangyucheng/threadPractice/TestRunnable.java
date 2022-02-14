package com.zkyouxi.zhangyucheng.threadPractice;

public class TestRunnable {
    public static void main(String[] args) {
        Runnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("create thread via Runnable");
    }
}
