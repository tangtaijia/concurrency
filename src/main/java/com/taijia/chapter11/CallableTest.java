package com.taijia.chapter11;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有返回值的线程测试.
 * User: taijia
 * Date: 2015/3/25
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public class CallableTest {
    public static void main(String[] args) {
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);
        MyData data = new MyData(10);
        // 创建3个有返回值的任务
        Callable<String> call1 = new MyCallable(data);
        Callable<String> call2 = new MyCallable(data);
        Callable<String> call3 = new MyCallable(data);
        // 执行任务并获取Future对象
        Future<String> f1 = pool.submit(call1);
        Future<String> f2 = pool.submit(call2);
        Future<String> f3 = pool.submit(call3);
        try {
            // 从Future 获取任务的返回值，并输出出来
            System.out.println("Call1 >>>" + f1.get());
            System.out.println("Call2 >>>" + f2.get());
            System.out.println("Call3 >>>" + f3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            pool.shutdown();
        }
    }
}

class MyCallable implements Callable<String> {

    private MyData data;


    MyCallable(MyData data) {
        this.data = data;
    }

    @Override
    public String call() throws Exception {
        System.out.println("当前线程：" + Thread.currentThread().getName());
        data.change();
        return data.getValue() + "任务返回的内容";
    }
}

class MyData {
    int value;

    MyData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int change() {
        synchronized (this) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value--;
        }
        return getValue();
    }
}
