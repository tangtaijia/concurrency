package com.taijia.chapter6;

/**
 * 结果读取
 * 测试 wait notifyAll
 * User: taijia
 * Date: 2015/3/22
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class ResultReader extends Thread {
    final Calculator cor;

    ResultReader(Calculator cor, String name) {
        super(name);
        this.cor = cor;
    }

    @Override
    public void run() {
        synchronized (cor) {
            System.out.println(Thread.currentThread() + " 等待计算结果");
            try {
                cor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " 计算结果为 " + cor.total);
        }
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        new ResultReader(c, "Thread A").start();
        new ResultReader(c, "Thread B").start();
        new ResultReader(c, "Thread C").start();
        c.start();
    }
}

/**
 * 计算线程
 */
class Calculator extends Thread {
    int total;

    @Override
    public void run() {
        synchronized (this) {
            System.out.println(Thread.currentThread() + " 开始计算");
            for (int i = 0; i < 100; i++)
                total += i;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 通知在this对象上等待是所有线程，让它们都从等待状态返回到可运行状态
            notifyAll();
            System.out.println(Thread.currentThread() + " 完成计算");
        }
    }
}
