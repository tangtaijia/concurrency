package com.taijia.chapter10;

/**
 * 死锁测试
 * User: taijia
 * Date: 2015/3/23
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class DeadLockTest {
    public static void main(String[] args) {
        DeadLockRisk dead = new DeadLockRisk();
        new MyThread(1,2,dead,"Thread A").start();
        new MyThread(3,4,dead,"Thread B").start();
        new MyThread(5,6,dead,"Thread C").start();
        new MyThread(7,8,dead,"Thread D").start();
    }
}

class MyThread extends Thread {
    private int a, b;
    private DeadLockRisk dead;

    MyThread(int a, int b, DeadLockRisk dead,String name) {
        super(name);
        this.a = a;
        this.b = b;
        this.dead = dead;
    }

    @Override
    public void run() {
        dead.read();
        dead.write(a, b);
    }
}

class DeadLockRisk {
    private static class Resource {
        public int value;
    }

    private final Resource resourceA = new Resource();
    private final Resource resourceB = new Resource();

    /**
     * 读取
     *
     * @return 资源A B 的值之和
     */
    public int read() {
        synchronized (resourceA) {
            System.out.println("执行read()时 " + Thread.currentThread().getName() + " 获取resourceA的锁");
            synchronized (resourceB) {
                System.out.println("执行read()时 " + Thread.currentThread().getName() + " 获取resourceB的锁");
                System.out.println("读取和为 " + resourceA.value + resourceB.value);
                return resourceA.value + resourceB.value;
            }
        }
    }

    /**
     * 写入
     *
     * @param a 值a
     * @param b 值b
     */
    public void write(int a, int b) {
        synchronized (resourceB) {
            System.out.println("执行read()时 " + Thread.currentThread().getName() + " 获取resourceB的锁");
            synchronized (resourceA) {
                System.out.println("执行read()时 " + Thread.currentThread().getName() + " 获取resourceA的锁");
                System.out.println("写入为 A:" + resourceA.value +", B:"+ resourceB.value);
                resourceA.value = a;
                resourceB.value = b;
            }
        }
    }
}
