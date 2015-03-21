package com.taijia.chapter7;

/**
 * 线程优先级测试
 * User: taijia
 * Date: 2015/3/22
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class PriorityTest {
    public static void main(String[] args) {
        Thread t = new MyThread("MyThread");
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
        Thread r = new Thread(new MyRunnable(), "MyRunnable");
        r.setPriority(Thread.MAX_PRIORITY);
        r.start();
    }
}
