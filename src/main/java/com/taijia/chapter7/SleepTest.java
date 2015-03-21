package com.taijia.chapter7;

/**
 * 线程休眠测试
 * sleep 无法保证线程执行的顺序
 * User: taijia
 * Date: 2015/3/22
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class SleepTest {
    public static void main(String[] args) {
        new MyThread("MyThread").start();
        new Thread(new MyRunnable(), "MyRunnable").start();
    }
}

class MyThread extends Thread {
    MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " 第" + i + "次执行！");
        }
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " 第" + i + "次执行！");
        }
    }
}
