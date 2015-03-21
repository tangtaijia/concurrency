package com.taijia.chapter8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试单线程的线程池、固定大小线程池、可缓存的线程池
 * User: taijia
 * Date: 2015/3/22
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
public class ThreadExecutorTest {

    public static void main(String[] args) {
        ExecutorService es = null;
        // 创建一个单线程的线程池
        es = Executors.newSingleThreadExecutor();
        // 创建一个固定大小线程池
        es = Executors.newFixedThreadPool(5);
        // 创建一个可缓存的线程池
        es = Executors.newCachedThreadPool();
        // 把线程放入池中进行执行
        es.execute(new MyThread("Thread A"));
        es.execute(new MyThread("Thread B"));
        es.execute(new MyThread("Thread C"));
        es.execute(new MyThread("Thread D"));
        es.execute(new MyThread("Thread E"));
        // 关闭线程池
        es.shutdown();
    }

}
