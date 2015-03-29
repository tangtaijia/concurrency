package com.taijia.chapter14;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阻塞队列测试.实现生产者消费者 FIFO
 * User: taijia
 * Date: 2015/3/26
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        // 声明一个容量为10的缓存队列
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);

        Producer producer1 = new Producer(blockingQueue,"Producer A");
        Producer producer2 = new Producer(blockingQueue,"Producer B");
        Producer producer3 = new Producer(blockingQueue,"Producer C");
        Consumer consumer = new Consumer(blockingQueue,"Consumer");

        // 声明线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(producer1);
        pool.execute(producer2);
        pool.execute(producer3);
        pool.execute(consumer);

        try {
            // 执行3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer1.stop();
        producer2.stop();
        producer3.stop();
        System.out.println("！！！！！！！！！！所有生产线程停止！！！！！！！！！！");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}

class Consumer implements Runnable {

    private BlockingQueue<String> queue;
    private String name;

    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Consumer(BlockingQueue<String> queue,String name) {
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("线程: " + name + "启动消费者线程！");
        Random random = new Random();
        boolean isRunning = true;
        try {
            while (isRunning) {
                System.out.println("线程: " + name + "正在从对列取数据...");
                String data = queue.poll(2, TimeUnit.SECONDS);
                if (!StringUtils.isEmpty(data)) {
                    System.out.println("线程: " + name + "正在消费数据：" + data);
                    Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                } else {
                    // 超过2秒还没数据，表明生产线程已经退出，自动退出消费线程
                    isRunning = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("线程: " + name + "退出消费者线程！");
        }

    }
}

class Producer implements Runnable {
    private volatile boolean isRunning = true;
    private BlockingQueue<String> queue;
    private AtomicInteger count = new AtomicInteger();
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
    private String name;

    Producer(BlockingQueue<String> queue,String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        String data;
        Random random = new Random();
        System.out.println("线程: " + name + "启动生产线程!");
        try {
            while (isRunning) {
                System.out.println("线程: " + name + "正在生产数据...");
                Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                data = "data:" + count.incrementAndGet();
                System.out.println("线程: " + name + "将数据：" + data + "放入队列");
                if (!queue.offer(data, 2, TimeUnit.SECONDS))
                    System.out.println("线程: " + name + "放入数据：" + data + " 失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("线程: " + name + "退出生产者线程！");
        }

    }

    public void stop() {
        isRunning = false;
    }
}
