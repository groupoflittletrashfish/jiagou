package com.noname.demo;

import lombok.*;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue的上网应用场景
 * Created by noname on 2019/8/7.
 */
public class Demo17 implements Runnable {
    private DelayQueue<Wangmin> queue = new DelayQueue<>();
    private boolean yinye = true;

    /**
     * 上机方法
     *
     * @param name
     * @param id
     * @param money
     */
    private void shangji(String name, String id, int money) {
        Wangmin man = new Wangmin(name, id, 1000 * money + System.currentTimeMillis());     //单位是毫秒，所以要x1000，还必须加上当前时间，这样才能到指定时间后自动移除队列
        System.out.println("网名:" + name + ",身份证:" + id + ",交钱:" + money + ",开始上网....");
        this.queue.add(man);
    }

    private void xiaji(Wangmin man) {
        System.out.println("网名:" + man.getName() + ",下机....");
    }

    /**
     * 不断从队列中获取被移除的元素，也就相当于网费用完的顾客
     */
    @Override
    public void run() {
        while (yinye) {
            try {
                Wangmin man = this.queue.take();
                xiaji(man);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("网吧开始营业....");
        Demo17 wangba = new Demo17();
        Thread thread = new Thread(wangba);
        thread.start();

        wangba.shangji("noname", "123", 10);
        wangba.shangji("binbin", "456", 1);
        wangba.shangji("pingtou", "789", 5);


    }
}

@Getter
@Setter
@ToString
@RequiredArgsConstructor
class Wangmin implements Delayed {      //必须实现Delayed接口
    @NonNull
    private String name;
    @NonNull
    private String id;
    @NonNull
    private long endTime;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 该方法用来判断是否已经到达结束时间，此处结束的时间点大于当前时间点，则表示结束
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return endTime - System.currentTimeMillis();
    }

    /**
     * 该接口只是排序接口，但必须得实现
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        Wangmin man = (Wangmin) o;
        return this.getDelay(this.timeUnit) - man.getDelay(this.timeUnit) > 0 ? 1 : 0;
    }
}