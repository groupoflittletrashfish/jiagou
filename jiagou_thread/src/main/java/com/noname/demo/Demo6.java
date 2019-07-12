package com.noname.demo;

/**
 * synchronized可以修饰对象本身时，不同线程对同一个类的加锁方法需要抢夺锁
 * Created by noname on 2019/7/12.
 */
public class Demo6 {

    public void meth1() {
        synchronized (this) {
            System.out.println("1....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Demo6 demo = new Demo6();
        new Thread(() -> {
            demo.meth1();
        }).start();

        new Thread(() -> {
            demo.meth1();
        }).start();
    }
}
