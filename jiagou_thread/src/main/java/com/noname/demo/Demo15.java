package com.noname.demo;

import java.util.concurrent.ConcurrentLinkedQueue;

/** ConcurrentLinkedQueue: 适用于高并发场景，性能优于BlockingQueue,遵顼先进先出原则，不支持null值
 *  核心方法：
 *          add()和offer()：添加元素，无任何区别
 *          poll()和peek(): 获取头部元素。区别：poll()会删除元素，peek不会
 *
 * Created by noname on 2019/8/5.
 */
public class Demo15 {

}
