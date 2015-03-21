package com.taijia.chapter8;

/**
 * Created with IntelliJ IDEA.
 * User: taijia
 * Date: 2015/3/22
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public class MyThread extends Thread {
    MyThread() {
    }

    MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + " 正在执行~~~");
    }
}
