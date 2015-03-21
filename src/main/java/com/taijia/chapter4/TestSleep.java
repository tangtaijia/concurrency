package com.taijia.chapter4;

/**
 * Created with IntelliJ IDEA.
 * User: taijia
 * Date: 2015/3/21
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public class TestSleep implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (0 == i % 10)
                System.out.println("================" + i);
            try {
                Thread.sleep(1);
                System.out.println(i + "   线程睡眠1毫秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new TestSleep()).start();
    }
}
