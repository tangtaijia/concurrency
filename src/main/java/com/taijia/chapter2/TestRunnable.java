package com.taijia.chapter2;

import com.alibaba.fastjson.JSON;

/**
 * 实现Runnable接口的类
 * User: taijia
 * Date: 2015/3/15
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class TestRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + ": " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new TestRunnable(), "阿三").start();
        new Thread(new TestRunnable(), "李四").start();
        int[] ia = {1, 2, 3, 4, 5};
        System.out.println(JSON.toJSONString(ia));
    }
}
