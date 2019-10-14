package com.noname.demo;

import java.nio.IntBuffer;

/**
 * Created by noname on 2019/9/23.
 * Buffer的简单使用，细节较多
 * 注意点： buf.flip(); 该方法很重要，需要是不是的归位游标
 * buffer中的position和cap很容易理解，具体解释下limit,
 * limit是用来记录下一次读取或者写入的位置,通俗来讲-->就是在flip()后记录最后一次postion
 * 1.初始化（容量为7的buffer）-->pos:0,limit=cap=7，此时必定等同于cap
 * 2.写入3个字节-->pos:3,limit:7,cap=7
 * 3.读取3个字节，必须要调用flip(),此时pos:0,limit:3,因为下一次写入的位置就是第三个索引开始
 * 4.遍历字节，当pos=limit的时候，那就意味着遍历完了整个缓冲
 * 5.调用clear()，可将指针恢复成初始化状态
 */
public class Demo2 {
    public static void main(String[] args) {
        IntBuffer buf = IntBuffer.allocate(10);         //10代表缓冲区的容量
        buf.put(13);        //pos:0-->1
        buf.put(21);        //pos:1-->2
        buf.put(35);        //pos:2-->3


        System.out.println(buf);    //输出的对象中有3个属性，pos:游标所在的位置，lim:最大限制数，cap:容量
        System.out.println(buf.get(1)); //获取下标为1的元素，get()方法并不会使游标位置发生改变
        buf.put(1, 4);      //将下标为1的数据替换为4
        System.out.println("当前游标的位置:" + buf.position());    //put方法并不会改变游标的位置

        buf.flip();     //复位，即游标归零
        for (int x = 0; x < buf.limit(); x++) {     //遍历之前需要先游标归零，因为遍历是从当前游标开始的
            System.out.println(buf.get());
        }

        //wrap()的使用
        int[] arr = {1, 2, 5};
        IntBuffer buf1 = IntBuffer.wrap(arr);       //将数组的元素刷如缓冲对象
        System.out.println(buf1);

        IntBuffer buf2 = IntBuffer.wrap(arr, 0, 2);     //0代表起始位置，2代表元素个数
        System.out.println(buf2);


        //duplicate()复制方法
        IntBuffer buf3 = buf1.duplicate();
        System.out.println(buf3);
        //position()设置游标的位置
        buf1.position(1);
        System.out.println("改变游标位置:"+buf1);
        //获取可读数据数量，指的是游标与limit之间的数据
        System.out.println(buf1.remaining());

    }
}
