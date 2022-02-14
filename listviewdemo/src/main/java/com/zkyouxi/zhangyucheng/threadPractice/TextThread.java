package com.zkyouxi.zhangyucheng.threadPractice;

public class TextThread{
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();
    }

}

class MyThread extends Thread{
    @Override
    public void run() {
        super.run();
        System.out.println("Thread started");
    }
}
