package com.taijia.chapter8;

import com.alibaba.fastjson.JSON;

/**
 * 同步测试
 * User: taijia
 * Date: 2015/3/22
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        User u = new User("张三", 100);
        new SyncThread(u, 2, "Thread A").start();
        new SyncThread(u, -11, "Thread B").start();
        new SyncThread(u, -25, "Thread C").start();
        new SyncThread(u, 13, "Thread D").start();
        new SyncThread(u, 37, "Thread E").start();
    }
}

class SyncThread extends Thread {
    private User u;
    private int y = 0;

    SyncThread(User u, int y, String name) {
        super(name);
        this.u = u;
        this.y = y;
    }

    @Override
    public void run() {
        u.oper(y);
    }
}

class User {

    private String name;

    private int cash; // 账户余额设为私有变量，禁止直接访问

    User(String code, int cash) {
        this.name = code;
        this.cash = cash;
    }

    public String getName() {
        return name;
    }

    public int getCash() {
        return cash;
    }

    // synchronized 可同步方法和代码块
    public void oper(int x) {
        try {
            synchronized (this) {
                this.cash += x;
                System.out.println(Thread.currentThread().getName() +
                        " 运行结束，增加 " + x + "，当前用户" + this + "余额为 " + cash);
            }
            // 在使用synchronized关键字时候，应该尽可能避免在synchronized方法或synchronized块中使用sleep或者yield方法，
            // 因为synchronized程序块占有着对象锁，你休息那么其他的线程只能一边等着你醒来执行完了才能执行。
            // 不但严重影响效率，也不合逻辑
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
//        return JSON.toJSONString(this); // 这样貌似会新启一个线程
        return "User{" + "code='" + name + "', cash=" + cash + "}";
    }
}
