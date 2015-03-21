package com.taijia.chapter5;

/**
 * 线程同步测试.
 * User: taijia
 * Date: 2015/3/21
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class MyRunnable implements Runnable {
    private final Foo foo = new Foo();

    @Override
    public void run() {
        // 原文不妥，感觉实在调用的地方来加锁，而不是在方法里面加锁
        // 因为原文的方式只能控制fix()是同步的，而不能控制调用它的地方是同步的
        for (int i = 0; i < 3; i++) {
            synchronized (foo) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fix(30,Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + ":当前foo对象的x值是 " + foo.getX());
            }
        }
    }

    public static void main(String[] args) {
        Runnable r = new MyRunnable();
        new Thread(r, "Thread A ").start();
        new Thread(r, "Thread B ").start();
    }

    public void fix(int y,String name) {
        foo.fix(y,name);
    }
}

class Foo {
    private int x = 100;

    public int getX() {
        return x;
    }

    public void fix(int y, String name) {
        // 动态方法锁 this
        // 静态对象锁 Class
        x = x - y;
        System.out.println(name + " call function fix to fix " + y);
    }
}