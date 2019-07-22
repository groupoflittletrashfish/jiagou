package com.noname.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的使用，一般需要在等待的地方使用await方式，直到计数为0时获得锁
 * Created by noname on 2019/7/22.
 */
public class Demo11 {
    final static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static volatile List<String> list = new ArrayList<>();

    public void add(String ele) {
        list.add(ele);
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int x = 0; x < 10; x++) {
                list.add(String.valueOf(x));
                System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.size() == 5) {
                    countDownLatch.countDown();         //如size==5，则计数-1，由于计数初始化为1，此时归零，t2获得锁
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("t2执行...");
                countDownLatch.await();     //当t2刚运行时，让t2等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2收到通知，线程停止");
            throw new RuntimeException("停止");
        }, "t2");

        t2.start();     //还是需要等待的线程先运行
        t1.start();
    }
}
