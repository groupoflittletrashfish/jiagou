package com.noname.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** wait和notify实现阻塞队列
 * Created by noname on 2019/7/22.
 */
public class Demo12 {

    private static List<Object> list = new LinkedList<>();          //初始化容器
    private AtomicInteger count = new AtomicInteger(0);
    private static final int MAX = 5;           //容器最大值，建议使用构造函数初始化
    private static final int MIN = 0;           //容器的最小值
    final Object lock = new Object();           //锁对象

    /**
     * 向队列中插入一个值
     *
     * @param object
     * @throws InterruptedException
     */
    public void put(Object object) throws InterruptedException {
        synchronized (lock) {
            while (count.get() == this.MAX) {           //若计数已经到达最大值，即容器已经满了的情况下,需要等待
                lock.wait();
            }
            list.add(object);       //若队列未满，则添加元素
            System.out.println("新加入元素:" + object);
            count.incrementAndGet();    //自增
            lock.notify();              //关键:通知其他线程已经操作完毕
        }
    }

    /**
     * 从队列中获取最新的值
     *
     * @return
     */
    public Object take() throws InterruptedException {
        Object ret = null;
        synchronized (lock) {
            while (count.get() == this.MIN) {      //如果取值的时候队列为空，则进入等待状态
                lock.wait();
            }
            ret = list.remove(0);         //移除第一个元素
            count.decrementAndGet();    //计数器递减
            lock.notify();              //关键:通知其他线程已经操作完毕
        }
        return ret;

    }


    public static void main(String[] args) throws InterruptedException {
        Demo12 demo = new Demo12();
        demo.put(1);
        demo.put(2);
        demo.put(3);
        demo.put(4);
        demo.put(5);
        System.out.println("当前队列长度:" + demo.getSize());
        Thread t1 = new Thread(() -> {
            try {
                while (true) {      //循环添加元素，若队列已满，线程等待并且释放锁
                    demo.put(6);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                while (true) {      //循环获取元素，若列表为空则释放锁
                    Thread.sleep(1000);
                    Object take = demo.take();
                    System.out.println("移出元素：" + take);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        t2.start();
    }

    private int getSize() {
        return count.get();
    }
}
