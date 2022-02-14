package com.zkyouxi.zhangyucheng.threadPractice;

public class ThreadShareVariable {
    public static void main(String[] args) {
        Runnable runnable = new ShareVariableRunnable();
        Thread[] threads = new Thread[5];
        for(int i = 0; i < 5; i++){
            threads[i] = new Thread(runnable, "thread: " + (i+1));
            testCount();
        }
        for(Thread thread : threads){
            thread.start();
        }
    }

    private static void testCount(){
        int count = 5;
        System.out.println(count--);
    }
}

class ShareVariableRunnable implements Runnable{
    private int count = 5;

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ",count: " + count--);
    }
}
