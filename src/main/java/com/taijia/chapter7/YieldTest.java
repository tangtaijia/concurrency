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
            // 在同步程序块内调用yeild方法让出CPU资源也没有意义，因为你占用着锁，其他互斥线程还是无法访问同步程序块
            if (0 == i % 7)
                Thread.yield();
        }
    }
}