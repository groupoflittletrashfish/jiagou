package com.noname.demo;

import java.util.concurrent.*;

/**
 * Created by noname on 2019/8/14.
 */
public class Demo24 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> callable = () -> {     //新建一个任务
            Thread.sleep(3000);
            return "任务完成...";
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> future1 = executorService.submit(callable);      //提交并运行任务，通过返回的Future对象，可以获取返回值
        System.out.println("请求完毕");
        //该方法很重要，get()会一直阻塞直到线程运行完成
        System.out.println(future1.get());
        System.out.println("线程是否运行完成:" + future1.isDone());
        System.out.println("---------------------");
        executorService.shutdown();
    }
}
