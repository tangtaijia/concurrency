package com.taijia.chapter9;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者
 * <p/>
 * 1、生产者仅仅在仓储未满时候生产，仓满则停止生产。
 * <p/>
 * 2、消费者仅仅在仓储有产品时候才能消费，仓空则等待。
 * <p/>
 * 3、当消费者发现仓储没产品可消费时候会通知生产者生产。
 * <p/>
 * 4、生产者在生产出可消费产品时候，应该通知等待的消费者去消费。
 * <p/>
 * User: taijia
 * Date: 2015/3/22
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class ProducerConsumer {
    public static void main(String[] args) {
//        Godown godown = new Godown(); // synchronized wait notify notifyAll
//        Godown godown = new Storage(); // lock condition await signal signalAll
        Godown godown = new Warehouse(); // LinkedBlockingQueue
        // 生产线程
        for (int i = 0; i < 16; i++) {
            new Producer(13, godown).start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 消费线程
        for (int i = 0; i < 18; i++) {
            new Consumer(12, godown).start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 仓库--synchronized wait notify notifyAll
 */
class Godown {
    public static final int MAX_SIZE = 77; // 最大库存量
    protected int currnum; // 当前库存量

    Godown() {
        this.currnum = 0;
    }

    Godown(int currnum) {
        this.currnum = currnum;
    }

    /**
     * 生产
     *
     * @param num 个数
     */
    public void produce(int num) {
        synchronized (this) {
            // 查看仓库是否将超过最大库存
            while ((currnum + num) > MAX_SIZE) {
                System.out.println("当前库存为 " + currnum + ",要生产的产品数量 " + num +
                        " 超过了最大库存数量" + MAX_SIZE + "，暂时不能自行生产！！");
                try {
                    // 当前生产线程等待
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 生产，这里只是简单的增加当前库存数量
            currnum += num;
            System.out.println("生产了 " + num + "个产品，现仓储量为 " + currnum);
            // 唤醒此对象上所有等待的线程
            notifyAll();
        }
    }

    /**
     * 消费
     *
     * @param num 个数
     */
    public void consume(int num) {
        synchronized (this) {
            // 测试是否可消费
            while (num > currnum) {
                System.out.println("当前库存为 " + currnum + ",要消费的产品数量 " + num + " ，暂时不够消费！！");
                try {
                    // 当前消费线程等待
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 消费，这里只是简单的减少当前库存数量
            currnum -= num;
            System.out.println("消费了 " + num + "个产品，现仓储量为 " + currnum);
            // 唤醒此对象上等待的所有线程
            notifyAll();
        }
    }
}

/**
 * 仓库--lock condition await signal signalAll
 */
class Storage extends Godown {
    Storage() {
        super();
    }

    Storage(int currnum) {
        super(currnum);
    }
    // 锁
    private final Lock lock = new ReentrantLock(true); // true的话就是公平锁
    // 仓库满的条件
    private final Condition full = lock.newCondition();
    // 仓库空的条件
    private final Condition empty = lock.newCondition();

    /**
     * 生产
     * @param num 个数
     */
    @Override
    public void produce(int num) {
        // 获得锁
        if(lock.tryLock()) {
            // 查看仓库是否将超过最大库存
            while ((currnum + num) > MAX_SIZE) {
                System.out.println("当前库存为 " + currnum + ",要生产的产品数量 " + num +
                        " 超过了最大库存数量" + MAX_SIZE + "，暂时不能自行生产！！");
                try {
                    // 当前生产线程等待
                    full.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 生产，这里只是简单的增加当前库存数量
            currnum += num;
            System.out.println("生产了 " + num + "个产品，现仓储量为 " + currnum);
            empty.signalAll(); // 唤醒因仓库空而阻塞的线程
            // 释放锁
            lock.unlock();
        } else {
            System.out.println("生产者线程：" +Thread.currentThread().getName() + " 获取锁失败！");
        }
    }

    /**
     *
     * @param num 个数
     */
    @Override
    public void consume(int num) {
        // 获得锁
        if(lock.tryLock()) {
            // 测试是否可消费
            while (num > currnum) {
                System.out.println("当前库存为 " + currnum + ",要消费的产品数量 " + num + " ，暂时不够消费！！");
                try {
                    // 当前消费线程等待
                    empty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 消费，这里只是简单的减少当前库存数量
            currnum -= num;
            System.out.println("消费了 " + num + "个产品，现仓储量为 " + currnum);
            full.signalAll(); // 唤醒因仓库满而阻塞的线程
            // 释放锁
            lock.unlock();
        } else {
            System.out.println("消费者线程："+Thread.currentThread().getName() + " 获取锁失败！");
        }
    }
}

/**
 * 仓库--LinkedBlockingQueue
 */
class Warehouse extends Godown {
    // 仓库存储的载体
    private LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<Object>(MAX_SIZE);

    /**
     * 生产
     * @param num 个数
     */
    @Override
    public void produce(int num) {
        // 如果库存量满
        if(MAX_SIZE < list.size()+num) {
            System.out.println("当前库存为 " + list.size() + ",要生产的产品数量 " + num +
                    " 超过了最大库存数量" + MAX_SIZE + "，暂时不能自行生产！！");
        }
        // 生产条件满足情况下，生产num个产品
        for (int i = 0; i < num; i++) {
            try {
                // 放入产品并自动阻塞
                list.put(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产了 " + num + "个产品，现仓储量为 " + list.size());
    }

    /**
     * 消费
     * @param num 个数
     */
    @Override
    public void consume(int num) {
        // 如果库存量空
        if(list.size() < num)
            System.out.println("当前库存为 " + list.size() + ",要消费的产品数量 " + num + " ，暂时不够消费！！");
        // 消费条件满足情况下，消费num个产品
        for (int i = 0; i < num; i++) {
            try {
                // 消费产品并自动阻塞
                list.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("消费了 " + num + "个产品，现仓储量为 " + list.size());
    }
}

/**
 * 生产者
 */
class Producer extends Thread {
    private int num; // 要生产的数量
    private Godown godown;

    Producer(int num, Godown godown) {
        this.num = num;
        this.godown = godown;
    }

    @Override
    public void run() {
        godown.produce(num); // 生产num个产品
    }
}

/**
 * 消费者
 */
class Consumer extends Thread {
    private int num; // 要消费的的产品的数量
    private Godown godown;

    Consumer(int num, Godown godown) {
        this.num = num;
        this.godown = godown;
    }

    @Override
    public void run() {
        godown.consume(num); // 消费num个产品
    }
}