package com.noname.demo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by noname on 2019/8/12.
 */
public class Demo18_Worker implements Runnable {

    private ConcurrentLinkedQueue<Demo18_Task> workQueue;
    private ConcurrentHashMap<String, Object> resultMap;

    @Override
    public void run() {
        while (true) {
            Demo18_Task input = workQueue.poll();   //不断从队列中获取任务
            if (input == null) {        //如果该队列已经无任务了，就跳出死循环
                break;
            }
            Object output = handle(input);
            this.resultMap.put(String.valueOf(input.getId()), output);
        }

    }

    /**
     * 模拟不同的任务操作，假设任务耗时0.5s
     *
     * @param input
     * @return
     */
    private Object handle(Demo18_Task input) {
        Object output = null;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input.getPrice();
    }

    public void setWorkQueue(ConcurrentLinkedQueue<Demo18_Task> workQueue) {
        this.workQueue = workQueue;
    }

    public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}
