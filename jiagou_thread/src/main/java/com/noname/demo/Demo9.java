package com.noname.demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile只能保证数据在其他线程可见，但是无法像synchronized一样保证每个线程拿到的值是最新的。虽然两条线程都能获取到值
 * 但是在线程对count频繁递增的时候，依旧会出现A线程和B线程取到同一个相同的值C，两个线程都对C递增了一次后写入了内存，造成数据错误
 * <p>
 * 解决：使用AtomicInteger类型来操作递增,或者在方法上加锁，推荐前者
 * <p>
 * Created by noname on 2019/7/18.
 */
public class Demo9 implements Runnable {

    static volatile int count = 0;  //解决：static volatile AtomicInteger count = new AtomicInteger(0);

    private void add() {
        for (int x = 0; x < 1000; x++) {
            count++;                //解决：count.incrementAndGet();
        }
    }

    @Override
    public void run() {
        add();
    }


    public static void main(String[] args) throws InterruptedException {
        Demo9 demo = new Demo9();
        new Thread(demo).start();
        new Thread(demo).start();
        Thread.sleep(5000);     //5秒后以上操作一定是可以执行完的
        System.out.println(demo.count);
    }
}
