package com.noname.demo;

/**
 * 结论：如果多条线程调用的是同一个对象，该对象中所有的加锁的方法共享同一把锁。即A线程调用该类的A方法，B线程调用该类的B方法，
 *      但是锁是针对该对象而言只有一把，B线程即使不调用A方法也是需要等待A方法执行完毕才可以获得锁
 *
 *  若去掉method1或者method2方法上的synchronized则不存在抢锁的情况，除非多个线程调用的是同一个加锁的方法，才会抢锁
 * Created by noname on 2019/7/9.
 */
public class Demo1 {

    public synchronized void method1()  {
        System.out.println("1.....");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void method2(){
        System.out.println("2.....");
    }

    public static void main(String[] args) {
        Demo1 demo=new Demo1();

        new Thread(new Runnable() {
            @Override
            public void run() {
                demo.method1();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                demo.method2();
            }
        }).start();
    }



}
