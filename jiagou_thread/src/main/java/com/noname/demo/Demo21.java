package com.noname.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by noname on 2019/8/13.
 *
 * newScheduledThreadPool的简单使用
 */
public class Demo21 extends Thread {
    @Override
    public void run() {
        System.out.println("run");
    }
}

class ScheduledJob {
    public static void main(String[] args) {
        Demo21 comand = new Demo21();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //1秒后开始执行该类的run方法，每3秒执行一次
        scheduledExecutorService.scheduleWithFixedDelay(comand, 1, 3, TimeUnit.SECONDS);
    }
}
