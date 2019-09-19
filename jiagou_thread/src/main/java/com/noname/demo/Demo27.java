package com.noname.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by noname on 2019/9/17.
 * <p>
 * Condition的使用。await()和signal()分别对应wait()和notify()方法
 * Condition较为灵活，因为一个lock可以创建多个Condition，这样可以控制多个线程的休眠和唤醒
 */
public class Demo27 {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void method1() {
        lock.lock();
        System.out.println("当前线程：" + Thread.currentThread().getName() + "进入等待...");
        try {
            Thread.sleep(2000);
            condition.await();              //该线程进入等待，等待唤醒，并且释放锁
            System.out.println("当前线程：" + Thread.currentThread().getName() + "继续执行...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();          //必须释放锁
        }
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出method1...");
    }


    public void method2() {
        lock.lock();
        System.out.println("当前线程：" + Thread.currentThread().getName() + "进入...");
        try {
            Thread.sleep(3000);
            condition.signal();         //唤醒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Demo27 demo = new Demo27();
        Thread t1 = new Thread(demo::method1, "t1");
        Thread t2 = new Thread(demo::method2, "t2");

        t1.start();
        t2.start();
    }
}
