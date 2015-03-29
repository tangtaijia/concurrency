package com.taijia.chapter15;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 阻塞队列 - LIFO.
 * User: taijia
 * Date: 2015/3/29
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
public class LinkedBlockingDequeTest {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingDeque<String>(20);
        int index=0;
        while(true) {
            try {
                String data = "向阻塞栈钟添加元素 " + index++;
                queue.put(data);
                System.out.println(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
