package com.noname.demo;

/**
 * 在多线程中，如果使用普通的单例模式是不合适的，更建议使用内部静态类的形式
 * 另外一种方式为double-check方式，此处不做介绍。
 * Created by noname on 2019/8/5.
 */
public class Demo13 {

    private static class Singletion {                   //内部类,内部类中设置一个静态变量来创建自己
        private static Singletion single = new Singletion();
    }

    public static Singletion getInstance() {            //返回实体的单例
        return Singletion.single;
    }
}
