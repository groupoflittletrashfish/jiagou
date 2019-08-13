package com.noname.demo;

import lombok.*;

import java.util.Iterator;
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
        Wangmin man = new Wangmin(name, id, money * 1000 + System.currentTimeMillis());     //单位是毫秒，所以要x1000，还必须加上当前时间，这样才能到指定时间后自动移除队列
        System.out.println("网名:" + name + ",身份证:" + id + ",交钱:" + money + ",开始上网....");
        this.queue.add(man);
    }

    private void xiaji(Wangmin man) {
        System.out.println("网名:" + man.getName() + ",下机....");
    }

    private void lookQueue() {
        Iterator<Wangmin> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Wangmin next = iterator.next();
            System.out.println(next);
        }
    }

    /**
     * 不断从队列中获取被移除的元素，也就相当于网费用完的顾客
     */
    @Override
    public void run() {
        while (yinye) {
            try {
                Wangmin man = this.queue.take();            //此时若没有元素自动移除，则一直处于阻塞状态
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
        wangba.lookQueue();

    }
}

@Getter
@Setter
@ToString
@AllArgsConstructor
class Wangmin implements Delayed {      //必须实现Delayed接口
    private String name;
    private String id;
    private long endTime;

    /**
     * 该方法用来判断是否已经到达结束时间，此处结束的时间点一旦小于当前时间点，则表示结束，将会自动移除队列
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return endTime - System.currentTimeMillis();
    }

    /**
     * 必须实现排序，排序也必须根据业务需求来决定。
     * 由于此处是根据结束时间的长短来决定下机时间，所以排序为结束时间从小到大,如果排序方法写错，如改成从大到小，
     * 将会造成所有的网民都是以结束时间最长的一个为准，且同时下机。因为队列的顺序为从大到小，所以不能正常依次弹出
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        Wangmin man = (Wangmin) o;
        return this.endTime - man.getEndTime() > 0 ? 1 : -1;
    }
}