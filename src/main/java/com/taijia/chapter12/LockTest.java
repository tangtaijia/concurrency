package com.taijia.chapter12;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 锁测试
 * User: taijia
 * Date: 2015/3/25
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public class LockTest {
    public static void main(String[] args) {
        Account account = new Account("32545468545",1000);
        Lock lock = new ReentrantLock(true);
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new User("张三 自己",account,400,lock));
        pool.execute(new User("张三 老婆",account,-240,lock));
        pool.execute(new User("张三 儿子",account,-30,lock));
        pool.execute(new User("张三 老爸",account,310,lock));

        Account myAccount = new Account("3233243434",1000);
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        pool.execute(new SuperUser("李四 自己", myAccount, 400, readWriteLock, false));
        pool.execute(new SuperUser("李四 老婆", myAccount, 0, readWriteLock, true));
        pool.execute(new SuperUser("李四 儿子", myAccount, -30, readWriteLock, false));
        pool.execute(new SuperUser("李四 老爸", myAccount, 310, readWriteLock, false));
        pool.shutdown();
    }
}

/**
 * 信用卡用户
 */
class User implements Runnable {
    protected String name; // 用户名
    protected Account account; // 账户
    protected int opcash; // 操作的金额
    protected Lock myLock; // 执行操作所需的锁对象

    User(String name, Account account, int opcash, Lock myLock) {
        this.name = name;
        this.account = account;
        this.opcash = opcash;
        this.myLock = myLock;
    }

    /**
     * 执行业务
     * @param oper
     */
    protected void oper(String oper) {
        System.out.println(oper+name + "操作账户：" + account + "尝试====, 金额为：" + opcash + ", 账户余额为：" + account.getCash());
        account.setCash(account.getCash() + opcash);
        System.out.println(oper+name + "操作账户：" + account + "成功~~~~, 金额为：" + opcash + ", 账户余额为：" + account.getCash());
    }

    /**
     * 执行业务
     */
    protected void oper() {
        oper("");
    }

    @Override
    public void run() {
        // 获取锁
        myLock.lock();
        // 执行业务
        oper();
        myLock.unlock(); // 释放锁
    }
}

class SuperUser extends User {
    private boolean isRead; // 是否查询
    private ReadWriteLock myLock; // 读写锁

    SuperUser(String name, Account account, int opcash, ReadWriteLock myLock,boolean isRead) {
        super(name, account, opcash, null);
        this.isRead = isRead;
        this.myLock = myLock;
    }

    @Override
    public void run() {
        if(isRead) {
            // 获取读锁
            myLock.readLock().lock();
            System.out.println("读："+name+" 正在查询"+account+"账户，当前金额为："+account.getCash());
            // 释放读锁
            myLock.readLock().unlock();
        } else {
            // 获取写锁
            myLock.writeLock().lock();
            oper("写: "); // 执行操作
            // 释放写锁
            myLock.writeLock().unlock();
        }
    }
}

/**
 * 账户
 */
class Account {
    private String oid; // 账户编号
    private int cash; // 账户余额

    Account(String oid,int cash) {
        this.oid = oid;
        this.cash = cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getCash() {
        return cash;
    }

    public String getOid() {
        return oid;
    }

    @Override
    public String toString() {
        return "Account {" + "oid:\"" + oid + "\",cash:" + cash + "}";
    }
}
