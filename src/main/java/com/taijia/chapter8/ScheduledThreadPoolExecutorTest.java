package com.taijia.chapter8;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 无大小限制的线程池
 * User: taijia
 * Date: 2015/3/22
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() { // 每隔一段时间触发一次异常
            int i = 1;
            int count = 0;

            @Override
            public void run() {
                count += i;
                System.out.println("========================== " + count);
                if (0 == count % 3)
                    throw new RuntimeException();
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);

        exec.scheduleAtFixedRate(new Runnable() { // 每隔一段时间打印系统时间，证明两者互不影响
            @Override
            public void run() {
                System.out.println(new Date(System.currentTimeMillis()));
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }
}
