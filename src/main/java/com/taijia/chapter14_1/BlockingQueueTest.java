package com.taijia.chapter14_1;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 阻塞队列测试,
 * 如果BlockQueue是空的,从BlockingQueue取东西的操作将会被阻断进入等待状态,
 * 直到BlockingQueue进了东西才会被唤醒.
 * 同样,如果BlockingQueue是满的,任何试图往里存东西的操作也会被阻断进入等待状态,
 * 直到BlockingQueue里有空间才会被唤醒继续操作.
 * User: taijia
 * Date: 2015/3/29
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
public class BlockingQueueTest {
    /**
     * 定义装苹果的篮子
     */
    public static class Basket {
        // 能容纳3个苹果的篮子
        BlockingQueue<String> basket = new ArrayBlockingQueue<String>(3);

        /**
         * 放入篮子
         */
        public void produce(String apple) {
            try {
                basket.put(apple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 从篮子中取走
         * @return 苹果
         */
        public String consume() {
            try {
                return basket.poll(2, TimeUnit.SECONDS); // 2秒钟 拿不到 就返回空，而不阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 篮子剩余容量
         * @return 大小
         */
        public int leftRoom() {
            return basket.remainingCapacity();
        }
    }

    public static void testBasket() {
        /**
         * 篮子
         */
        final Basket basket = new Basket();

        /**
         * 生产者
         */
        class Producer implements Runnable {
            private boolean isRunning = true;
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        String apple = "Apple " + new Random().nextInt(1000);
                        basket.produce(apple);
                        System.out.println("生产者生产了一个苹果：" + apple + " , 还有 " + basket.leftRoom() + " 个空位");
                        Thread.sleep(500); // 休眠300毫秒
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void stop() {
                isRunning = false;
                System.out.println("生产者停止生产");
            }
        }

        /**
         * 消费者
         */
        class Consumer implements Runnable {
            @Override
            public void run() {
                try {
                    while (true) {
                        String apple = basket.consume();
                        if (StringUtils.isEmpty(apple)) {
                            System.out.println("啊哦，没苹果吃了哦~");
                            break;
                        }
                        System.out.println("消费者消费了一个苹果：" + apple + " , 还有 " + basket.leftRoom() + " 个空位");
                        Thread.sleep(200); // 休眠1000毫秒
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ExecutorService pool = Executors.newCachedThreadPool();
        Producer producer = new Producer();
        Runnable consumer = new Consumer();
        pool.submit(producer);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.submit(consumer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.stop();
        pool.shutdown();
        System.out.println("Shut down!!!");
    }

    public static void main(String[] args) {
        testBasket();
    }
}
