package com.noname.demo;

/**
 * 结论：synchronized的重入锁即使是在父子关系中也是可以正常执行且安全的。如下例子可以正常执行：不会出现抢夺锁的情况
 * Created by noname on 2019/7/12.
 */
public class Demo5 {
    static class A {
        int i = 10;

        synchronized void operA() throws InterruptedException {
            i--;
            System.out.println("A:" + i);
            Thread.sleep(1000);
        }
    }

    static class B extends A {
        synchronized void operB() throws InterruptedException {
            while (i > 0) {
                i--;
                System.out.println("B:" + i);
                Thread.sleep(1000);
                this.operA();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            B b = new B();
            try {
                b.operB();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
