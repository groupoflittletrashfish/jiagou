package com.noname.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by noname on 2019/8/5.
 * <p>
 * ArrayBlockingQueue: 基于数组的阻塞队列，所以是固定长度。可以指定先进先出或是先进入出.未实现读写分离，即读和写操作不能同时进行
 * LinkedBlockingQueue: 基于链表形式的阻塞队列，实现了读写分离且是无界队列
 * SynchronousQueue: 一种缓冲队列，不能存放任何元素。大致意思为：在任务较少的情况下，采用该队列直接将任务交付出去，而不像其他队列一样，需要暂存到队列中，以减少耗时
 * PriorityBlockingQueue: 基于优先级的队列，必须实现ComPator接口，所以队列的取出顺序是遵循实现的排序接口的
 * DelayQueue: 带有指定的延迟时间，无界队列。特性：指定时间后元素自动从队列中移出，场景应用：如缓存，任务超时处理，具体代码查看Demo17
 */
public class Demo16 {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);
        arrayBlockingQueue.offer("a", 2, TimeUnit.SECONDS);         //该方法是指在N的时间段内添加入队列，若成功则返回true
        arrayBlockingQueue.put("b");
        arrayBlockingQueue.add("c");
        arrayBlockingQueue.add("d");
        arrayBlockingQueue.add("e");
        List<String> list = new LinkedList<>();
        arrayBlockingQueue.drainTo(list, 3);        //该方法是从队列中取出N个元素放入集合中
        System.out.println(list);

        PriorityBlockingQueue<Task> priorityBlockingQueue = new PriorityBlockingQueue();        //泛型必须是实现了Comparable接口
        Task task1 = new Task();
        task1.setNum(4);
        Task task2 = new Task();
        task2.setNum(2);
        Task task3 = new Task();
        task3.setNum(3);
        Task task4 = new Task();
        task4.setNum(1);
        priorityBlockingQueue.add(task1);
        priorityBlockingQueue.add(task2);
        priorityBlockingQueue.add(task3);
        priorityBlockingQueue.add(task4);

        //直接输出队列，顺序可能并不是正确的排序结果，但是在调用take的时候，一定会选择优先级最高的元素，即按照正确的排序结果
        System.out.println(priorityBlockingQueue);
        System.out.println(priorityBlockingQueue.take());       //取出顺序遵循排序接口
        System.out.println(priorityBlockingQueue.take());
        System.out.println(priorityBlockingQueue.take());
        System.out.println(priorityBlockingQueue.take());

        //该队列较特殊，必须先take然后在add
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue();
        new Thread(() -> {
            String element = null;
            try {
                element = synchronousQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("取出元素：" + element);
        }).start();

        new Thread(() -> {
            synchronousQueue.add("1");
        }).start();

    }
}


@Getter
@Setter
@ToString
class Task implements Comparable<Task> {

    private int num;

    @Override
    public int compareTo(Task o) {
        return this.num - o.getNum();
    }


}
