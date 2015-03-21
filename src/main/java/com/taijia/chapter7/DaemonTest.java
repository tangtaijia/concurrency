package com.taijia.chapter7;

import java.util.TreeMap;

/**
 * 守护线程测试.
 * User: taijia
 * Date: 2015/3/22
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class DaemonTest {
    public static void main(String[] args) {
        Thread c = new MyCommon("Thread Common");
        Thread d = new MyDaemon("Thread Daemon");
        d.setDaemon(true);
        c.start(); // 看到前台线程执行完毕后，后台线程无论是否执行完，只要程序执行完，则算执行完，后台线程如果在运行状态则会强行关闭
        d.start();
    }
}

class MyCommon extends Thread {
    MyCommon(String name) {
        super(name);
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " 第" + i + "次执行！");
        }
    }
}

class MyDaemon extends Thread {
    MyDaemon(String name) {
        super(name);
    }
    @Override
    public void run() {
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " 第" + i + "次执行！Deamon!!!");
        }
    }
}
