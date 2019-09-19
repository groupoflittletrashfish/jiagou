package com.noname.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock较为简单，但凡使用ReentrantLock，基本都是try...finally，finally中必须释放锁
 * Created by noname on 2019/9/17.
 */
public class Demo26 {

    private Lock lock = new ReentrantLock();

    public void method1() {
        lock.lock();            //尝试获取锁，若未获得，则进入等待
        System.out.println("当前线程：" + Thread.currentThread().getName() + "进入method1...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();          //必须释放锁
        }
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出method1...");
    }

    public void method2() {
        lock.lock();
        System.out.println("当前线程：" + Thread.currentThread().getName() + "进入method2...");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出method2...");
    }

    public static void main(String[] args) {
        Demo26 demo = new Demo26();
        Thread t1 = new Thread(demo::method2, "t1");
        t1.start();

        Thread t2 = new Thread(demo::method1, "t2");
        t2.start();
    }

}
