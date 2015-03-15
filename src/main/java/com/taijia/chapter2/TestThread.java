package com.taijia.chapter2;

/**
 * 测试扩展Thread类实现的多线程程序
 * User: taijia
 * Date: 2015/3/15
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class TestThread extends Thread{
    public TestThread(String name) {
        super(name);
    }

    public void run() {
        for(int i = 0;i<5;i++){
            try {
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " :" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new TestThread("阿三").start();
        new TestThread("李四").start();
    }
}
