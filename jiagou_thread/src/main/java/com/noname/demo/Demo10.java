package com.noname.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 结论： wait和notify必须配合synchronized使用
 * wait-等待，直到在其他线程调用此对象的 notify() 方法或 notifyAll() 方法，会释放锁
 * notify-唤醒，唤醒在此对象监视器上等待的单个线程，但不会释放锁
 * <p>
 * <p>
 * Created by noname on 2019/7/22.
 */
public class Demo10 {

    private static volatile List<String> list = new ArrayList<>();

    public void add(String ele) {
        list.add(ele);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Demo10 demo = new Demo10();
        Thread t1 = new Thread(() -> {
            for (int x = 0; x < 10; x++) {
                demo.add(String.valueOf(x));
                System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            while (true) {
                if (list.size() == 5) {
                    throw new RuntimeException("停止");
                }
            }
        }, "t2");
    }
}


/**
 * wait 和 notify的共同使用，该代码有缺陷，即t1线程中notify由于不是放锁，所以不能实时通知t2线程。若想实时通知，参看Demo11
 */
class Demo10_solve {
    final static Object lock = new Object();
    private static volatile List<String> list = new ArrayList<>();

    public void add(String ele) {
        list.add(ele);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {       //由于t2使用了wait方法，锁并释放，t1获得锁
                for (int x = 0; x < 10; x++) {
                    list.add(String.valueOf(x));
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (list.size() == 5) {
                        System.out.println("已经发出通知");
                        lock.notify();      //当list==5的时候，唤醒等待线程(t2)，但由于notify并不是放锁，所以需要t1执行完成后才会执行t2
                    }
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                if (list.size() != 5) {
                    try {
                        lock.wait();    //刚开始的时候，list是空，wait会释放锁，让其他线程先行执行，此时t1开始执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2收到通知，线程停止");
                throw new RuntimeException("停止");
            }
        }, "t2");

        t2.start();     //必须t2先运行,t2是需要先获得锁的
        t1.start();
    }
}

