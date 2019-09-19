package com.noname.demo;

import java.util.concurrent.CountDownLatch;

/**
 * Created by noname on 2019/8/13.
 *
 * 较简单，不做描述
 */
public class Demo22 {
    final static CountDownLatch count = new CountDownLatch(2);

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("等待其他线程执行完成....");
            try {
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t1线程开始运行...");
        }).start();

        new Thread(() -> {
            System.out.println("t2线程开始执行...");
            try {
                Thread.sleep(5000);
                count.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("t3线程开始执行...");
            try {
                Thread.sleep(3000);
                count.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
