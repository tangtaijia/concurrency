package com.taijia.chapter7;

/**
 * 线程让步测试.
 * User: taijia
 * Date: 2015/3/22
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class YieldTest {
    public static void main(String[] args) {
        new Thread(new Yielder(), "Yielder1").start();
        new Thread(new Yielder(), "Yielder2~~~~~~").start();
    }
}

class Yielder implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            System.out.println(Thread.currentThread() + " 第" + i + "次执行");
            if (0 == i % 7)
                Thread.yield();
        }
    }
}