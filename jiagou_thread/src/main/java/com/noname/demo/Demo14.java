package com.noname.demo;

/**
 * ConcurrentHashMap的设计思路：将容器分段并分别加锁，最多支持16段，若不同线程的访问的段不同，则不存在等待锁的情况
 * CopyOnWrite的设计思路：在多线程的场景下，A线程增删改的操作中，会先将原容器复制一份，后续B线程将会从原有容器中读取数据。而当A线程完成更新后，
 * 容器的地址将会指向复制的新容器上，原容器被回收掉。
 * Created by noname on 2019/8/5.
 *
 * 适用范围：CopyOnWrite-读多写少
 */
public class Demo14 {

}
