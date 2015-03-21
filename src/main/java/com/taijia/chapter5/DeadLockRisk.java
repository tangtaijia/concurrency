package com.taijia.chapter5;

/**
 * 测试死锁.
 * User: taijia
 * Date: 2015/3/21
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class DeadLockRisk implements Runnable {

    private static class Resource {
        public int value;
    }

    private final Resource resourceA = new Resource();
    private final Resource resourceB = new Resource();

    /**
     * 读方法
     * @return
     */
    public int read() {
        synchronized (resourceA) {
            try {
                Thread.sleep(1000); // 睡一秒 助于演示死锁情况
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resourceB) {
                return resourceA.value + resourceB.value;
            }
        }
    }

    /**
     * 写方法
     * @param a
     * @param b
     */
    public void write(int a, int b) {
        synchronized (resourceB) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resourceA) {
                resourceA.value = a;
                resourceB.value = b;
            }
        }
    }

    @Override
    public void run() {
        if ("A".equals(Thread.currentThread().getName())) {
            System.out.println("call write");
            write(1, 2);
        }
        if ("B".equals(Thread.currentThread().getName())) {
            System.out.println("call read " + read());
        }
    }

    public static void main(String[] args) {
        Runnable r = new DeadLockRisk();
        new Thread(r,"A").start();
        new Thread(r,"B").start();
    }
}

