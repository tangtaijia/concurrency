package com.taijia.chapter13;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量测试.
 * User: taijia
 * Date: 2015/3/25
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        MyPool myPool = new MyPool(20);
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new MyThread("Thread A",myPool,3));;
        pool.execute(new MyThread("Thread B",myPool,12));;
        pool.execute(new MyThread("Thread C",myPool,7));;
        pool.shutdown();
    }
}

/**
 * 池
 */
class MyPool {
    /**
     * 信号量
     */
    private Semaphore semaphore;

    /**
     * 池的大小
     * @param size
     */
    MyPool(int size) {
        semaphore = new Semaphore(size);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
}

class MyThread extends Thread {
    private MyPool myPool; // 池
    private int x; // 申请的信号量大小

    MyThread(String name,MyPool myPool,int x) {
        super(name);
        this.myPool = myPool;
        this.x = x;
    }

    @Override
    public void run() {
        try {
            // 从此信号量获取给定数目的许可
            myPool.getSemaphore().acquire(x);
            // TODO 业务处理 略……
            System.out.println(Thread.currentThread().getName() + " 成功获取了" + x + "个许可");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            myPool.getSemaphore().release(x);
            System.out.println(Thread.currentThread().getName() + " 成功释放了" + x + "个许可");
        }
    }
}

