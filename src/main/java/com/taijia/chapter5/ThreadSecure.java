package com.taijia.chapter5;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 线程安全
 * User: taijia
 * Date: 2015/3/21
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class ThreadSecure {
    public static void main(String[] args) {
        final NameList nl = new NameList();
        nl.add("aaa");
        class NameDropper implements Runnable {
            @Override
            public void run() {
                System.out.println(nl.pop());
            }
        }
        new Thread(new NameDropper(),"Thread A").start();
        new Thread(new NameDropper(),"Thread B").start();
    }
}

class NameList {
    private List<String> nameList = Collections.synchronizedList(new LinkedList<String>());

    public synchronized void add(String name) {
        nameList.add(name);
    }

    public synchronized String pop() {
        return nameList.size() > 0 ? nameList.remove(0) : null;
    }
}
