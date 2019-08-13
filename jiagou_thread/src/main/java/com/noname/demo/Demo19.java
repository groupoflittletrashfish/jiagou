package com.noname.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by noname on 2019/8/12.
 * <p>
 * newFixedThreadPool: 可以指定固定数量的线程。若一个任务进入后有空闲线程则执行，无则进入暂存区等待空闲线程
 * newSingleThreadExecutor: 创建一个只有一条线程的线程池。
 * newCachedThreadPool: 该线程池没有容量限制。当任务进入后，会查看是否有空闲线程，有则执行，没有则创建。任务完成后若线程
 * 空闲60秒，则会被回收掉
 * newScheduledThreadPool: 指定固定长度的线程池，但该线程池支持定时任务
 */
public class Demo19 {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
    }
}
