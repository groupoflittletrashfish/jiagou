package com.noname.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by noname on 2019/8/16.
 *
 * Semaphore：信号量，限制同时调用任务的线程个数。
 */
public class Demo25 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(5);           //只能同时有5个线程同时访问
        for (int x = 0; x < 20; x++) {
            final int NO = x;
            executorService.submit(() -> {
                try {
                    semaphore.acquire();            //获得许可
                    System.out.println("Accessing :" + NO);
                    Thread.sleep((long) (Math.random() * 10000));
                    semaphore.release();            //释放许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }
}
