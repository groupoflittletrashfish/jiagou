package com.noname.demo;

/**
 * synchronized支持锁的重入，即同一个对象中，被修饰synchronized的方法中内部调用其他上锁的方法时候，是可以直接获取锁的
 * Created by noname on 2019/7/12.
 */
public class Demo4 {

    public synchronized void meth1() {
        System.out.println("method1...");
        meth2();
    }

    public synchronized void meth2() {
        System.out.println("method2...");
        meth3();
    }

    public synchronized void meth3() {
        System.out.println("method3...");
    }

    public static void main(String[] args) {
        Demo4 demo = new Demo4();
        new Thread(() -> {
            demo.meth1();
        }).start();
    }
}
