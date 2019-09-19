package com.noname.demo;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

/**
 * Created by noname on 2019/9/18.
 *
 * ReadLock和WriteLock：读锁和写索，这两把锁遵循如下原则：
 *  读读互斥，读写互斥，
 *  即如果两把读锁的情况下是可以共同执行的，但如果是有写锁，则将会进行锁的抢夺
 */
public class Demo28 {
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReadLock readLock = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public void read() {
        try {
            readLock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前线程：" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

    }

    public void write() {
        try {
            writeLock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前线程：" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        Demo28 demo = new Demo28();
        Thread t1 = new Thread(demo::read, "t1");
        Thread t2 = new Thread(demo::read, "t2");
        Thread t3 = new Thread(demo::write, "t3");

        t1.start();
//        t2.start();
        t3.start();
    }
}
