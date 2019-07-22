package com.noname.demo;

/**
 * 结论：每一条线程都会创建独立的内存空间，其用来存放主内存中的变量副本，以供该线程使用。若此时主内存中A的值发生更改，
 * 此时副本的值不会立即变更，此时将会造成此例的情况，running已经更改为false了，但程序还是会继续执行。
 *
 * 解决：变量上使用volatile，该线程所有使用该变量的地方都将从主内存中获取值
 *
 * Created by noname on 2019/7/15.
 */
public class Demo8 extends Thread{

    private boolean running = true;     //解决：添加volatile

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run(){
        System.out.println("进入方法..");
        while(running){
            //此处什么都不要填写
        }
        System.out.println("方法停止...");
    }

    public static void main(String[] args) throws InterruptedException {
        Demo8 demo = new Demo8();
        demo.start();
        Thread.sleep(3000);
        demo.setRunning(false);
        System.out.println("更改为false");
        Thread.sleep(1000);
        System.out.println(demo.running);
    }
}
