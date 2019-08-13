package com.noname.demo;

import lombok.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by noname on 2019/8/12.
 * <p>
 * 当4种线程池都不满足业务的情况下，可以自定义线程池，而4个线程池底层也是通过ThreadPoolExecutor对象实现的,如下：参数说明
 * corePoolSize：核心线程数，即线程池创建时候初始化了N个线程
 * maximumPoolSize：最大线程数
 * keepAliveTime：线程的最大时间
 * unit：时间单位
 * workQueue：当线程池满载时候，又有任务进入，则该任务会放入自定义的队列中，直到线程池有空闲线程
 * threadFactory：暂时不用分析
 * handler：当线程池满载，并且等待区域的队列（workQueue）也处于满载状态，此时又有任务进入，则将调用该hanler,即相当于拒绝数据的处理方法
 * <p>
 * 请查看下面的实例来分析（此处使用的为有界队列）
 * 1.运行一个任务时：由于线程池的核心线程数为1，则直接通过该线程去执行任务
 * 2.运行两个任务时：由于核心线程数为1，暂存的队列的容量是3，则会将任务暂存到容器，等待核心的线程数空闲（此处为1）执行完成后再取出调用
 * 3.运行五个任务时：此时核心线程数+暂存队列都已经满载，此处为1+3=4，此时最大容量就将生效，线程池将会在保证线程总数不超过最大值的前提下，创建一个新的线程来直接去调用任务。所以此时的结果是任务1和任务5同时执行
 * 4.运行六个任务时：此时最大线程+暂存队列都已经满载，此处为2+3=5，此时将会执行handler的拒绝策略，所以此时将会抛出异常
 * <p>
 * *若使用的是无界队列（最大线程数将会失效，因为当核心线程数使用完后，新的任务会不断的加入无界队列中，不会出现拒绝策略，直到内存消耗殆尽）
 */
public class Demo20 {

    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        Task t1 = new Task(1, "任务1");
        Task t2 = new Task(2, "任务2");
        Task t3 = new Task(3, "任务3");
        Task t4 = new Task(4, "任务4");
        Task t5 = new Task(5, "任务5");
        Task t6 = new Task(6, "任务6");

        executor.execute(t1);
        executor.execute(t2);
        executor.execute(t3);
        executor.execute(t4);
        executor.execute(t5);
        executor.execute(t6);
        executor.shutdown();

        //无界队列
        LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(5, 10, 120, TimeUnit.SECONDS.SECONDS, queue);
        for (int x = 0; x < 20; x++) {
            executor2.execute(new Task2());
        }
        Thread.sleep(1000);
        //由于核心线程数有5个，所以前5个任务将会直接调用，从第5个任务开始，后面的任务都将加入队列中，所以此时队列中有15个任务
        System.out.println("此时队列中有:" + queue.size());
        Thread.sleep(2000);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Task implements Runnable {
        private int id;
        private String name;

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("任务：" + id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Task2 implements Runnable {

        @Override
        public void run() {
            int temp = count.incrementAndGet();
            System.out.println("任务:" + temp);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


