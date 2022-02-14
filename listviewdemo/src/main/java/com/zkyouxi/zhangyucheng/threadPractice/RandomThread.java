package com.zkyouxi.zhangyucheng.threadPractice;

public class RandomThread {
    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        for (int i = 0;i < 10;i++){
            threads[i] = new Random("RandomThread: " + i);
        }
        for(Thread thread : threads){
            thread.start();
        }
    }
}

class Random extends Thread{

    public Random(String name){
        super(name);
    }

    @Override
    public void run() {
        try{
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
