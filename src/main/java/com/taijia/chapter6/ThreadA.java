package com.taijia.chapter6;

/**
 * 计算输出其他线程锁计算的数据
 * 测试 wait notify
 * User: taijia
 * Date: 2015/3/22
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */
public class ThreadA {

    public static void main(String[] args) {
        ThreadB tb = new ThreadB();
        tb.start();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (tb) {
            System.out.println("等待线程B完成计算");
            try {
                // tb调用wait时，main Thread 会立即释放在tb上的锁
                tb.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("tb 对象计算总和total是 " + tb.total);
        }
    }
}

/**
 * ThreadB 计算total=0+1+2+3...+99
 */
class ThreadB extends Thread {
    int total;

    @Override
    public void run() {
        System.out.println("ThreadB 开始计算");
        synchronized (this) {
            for (int i = 0; i < 100; i++)
                total += i;
            // 完成计算后，唤醒在ThreadB对象上等待的单个线程，ThreadA
            // 调用nodtify的时候，不会立即释放锁，要等同步方法走完后才能释放锁
            notify();
            try {
                // 就算睡眠了1秒 也不会释放锁，要等到synchronized方法块走完才能释放
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadB 完成计算");
        }
    }
}
