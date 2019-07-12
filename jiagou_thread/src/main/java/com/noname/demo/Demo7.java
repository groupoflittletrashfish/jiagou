package com.noname.demo;

/**
 * synchronized+类 ：只要是该类的对象，不管是new多少个，在调用方法的时候都将争夺锁
 * <p>
 * 可以扩展为对象锁，即Object,但是没有深入研究，也可以使用String做锁，但是需要注意，在方法内部不要改变该字符串。即以字符串A作为锁，
 * 方法内部将该字符串改为了B，此时，其他线程就不需要抢夺锁了，造成线程不安全
 * Created by noname on 2019/7/12.
 */
public class Demo7 {


    public static void main(String[] args) {
        A a = new A();
        A aa = new A();
        new Thread(() -> {
            a.meth();
        }).start();

        new Thread(() -> {
            aa.meth();
        }).start();
    }


}

class A {
    public void meth() {
        synchronized (A.class) {
            System.out.println("1...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
