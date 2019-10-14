package com.noname.demo;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by noname on 2019/9/26.
 * <p>
 * AIO: 异步NIO的简单实现。
 * 此为服务器端代码实现，其中对数据做处理是通过Demo4_ServerCompletionHandler去实现，
 * 每当一个客户端连接后，都会实例化一个Handler去做数据的处理
 *
 * 与NIO的区别：NIO是通过selector去轮询各个通道，并判断各通道的状态来决定读取写入。AIO则是当有线程进入后，交由
 *             定义的Hanler去处理，并且重新创建一个Handler来提供给下一个线程使用，相当于是递归，线程本身都是
 *             异步执行的
 */
public class Demo4_Server {
    private ExecutorService executorService;        //线程池
    private AsynchronousChannelGroup threadGroup;       //线程组,实际上是保存了N个客户的连接
    public AsynchronousServerSocketChannel assc;        //服务器通道

    public Demo4_Server(int port) {
        try {
            executorService = Executors.newCachedThreadPool();      //初始化线程池
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);       //初始化线程组
            assc = AsynchronousServerSocketChannel.open(threadGroup);   //创建服务通道
            assc.bind(new InetSocketAddress(port));         //绑定端口
            System.out.println("server.port :" + port);
            assc.accept(this, new Demo4_ServerCompletionHandler());     //阻塞，Handler中做的是数据处理
            Thread.sleep(Integer.MAX_VALUE);        //很重要，一直阻塞，不让服务停止
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo4_Server server = new Demo4_Server(8765);
    }

}
