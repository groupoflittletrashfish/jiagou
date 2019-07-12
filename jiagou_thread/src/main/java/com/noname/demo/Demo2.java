package com.noname.demo;

/**
 * 脏读：返回的数据与预期的数据不一致，此例是脏读形成的一个简单demo
 * 如果想要避免脏读，那么此例可以在set和get的方法上都加上锁，这样同一个对象的读写都将抢夺锁，即不能同时进行
 * Created by noname on 2019/7/10.
 */
public class Demo2 {

    private String data;

    public void setValue(String data, String otherData) {
        this.data = data;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.data = otherData;
    }

    public void getValue() {
        System.out.println("data:" + data);
    }

    public static void main(String[] args) throws InterruptedException {
        Demo2 demo = new Demo2();
        //子线程赋值，赋值方法中线程休眠2秒
        Thread t1 = new Thread(() -> {
            demo.setValue("111", "222");
        });
        t1.start();
        //此时主线程花了一秒去获取查询结果，所以得到的值是setValue的第一个值（111），但是由于setValue方法有休眠2秒后再次赋值的操作，所以形成了脏读
        t1.sleep(1000);
        demo.getValue();

    }
}
