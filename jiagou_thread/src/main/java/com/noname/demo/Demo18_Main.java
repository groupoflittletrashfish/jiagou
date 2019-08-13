package com.noname.demo;

import java.util.Date;
import java.util.Random;

/**
 * Created by noname on 2019/8/12.
 */
public class Demo18_Main {

    public static void main(String[] args) {
        System.out.println("任务开始时间:" + new Date());
        Demo18_Master master = new Demo18_Master(new Demo18_Worker(), 10);
        Random random = new Random();
        for (int x = 0; x < 100; x++) {         //初始化需求，假设有100个任务
            Demo18_Task task = new Demo18_Task();
            task.setId(x);
            task.setName("任务:" + x);
            task.setPrice(random.nextInt(1000));
            master.submit(task);
        }

        master.execute();           //让Master唤起每个Worker
        while (true) {
            if (master.isCompleted()) {
                int result = master.getResult();
                System.out.println(result);
                System.out.println("任务结束时间:" + new Date());
                break;
            }
        }

    }
}
