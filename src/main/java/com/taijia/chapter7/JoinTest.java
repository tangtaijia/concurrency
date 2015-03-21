package com.taijia.chapter7;

/**
 * 线程合并测试.
 * User: taijia
 * Date: 2015/3/22
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class JoinTest {
    public static void main(String[] args) {
        Thread t = new MyThread("My Thread");
        t.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread() + " 第" + i + "次执行！");
            if (i > 2) {
                // 线程t 合并到main, main 停止执行，转而执行t，等t执行完毕后再执行main
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
