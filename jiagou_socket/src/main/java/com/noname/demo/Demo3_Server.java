package com.noname.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by noname on 2019/9/25.
 * NIO-服务端的固定套路
 * 其工作原理是在一个线程中去执行多路复用器（与传统的IO不同，传统IO一般是在建立连接后开启多个线程）,
 * 而通过多路复用器去轮询注册在上面的通道，检查通道的状态
 */
public class Demo3_Server implements Runnable {
    private Selector selector;
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);

    public Demo3_Server(int port) {
        try {
            selector = Selector.open();                    //打开多路复用器
            ServerSocketChannel ssc = ServerSocketChannel.open();       //打开服务器通道
            ssc.configureBlocking(false);                         //设置管道为非阻塞方式
            ssc.bind(new InetSocketAddress(port));                      //绑定端口
            ssc.register(this.selector, SelectionKey.OP_ACCEPT);        //注册到多路复用器，并监听阻塞事件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.selector.select();     //必须要让多路复用器开始监听
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();  //获取迭代器
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isValid()) {        //检查key是否有效
                        if (key.isAcceptable()) {       //判断是否为阻塞状态，若为阻塞状态则打开通道
                            this.accept(key);
                        }
                        if (key.isReadable()) {         //判断是否是可读状态
                            this.read(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void read(SelectionKey key) {
        try {
            this.readBuf.clear();       //清空缓冲区的数据
            SocketChannel sc = (SocketChannel) key.channel();   //获取客户端的通道
            int count = sc.read(this.readBuf);      //读取数据
            if (count == -1) {      //若没有数据则关闭
                key.channel().close();
                key.cancel();
                return;
            }
            this.readBuf.flip();        //有数据则读取，读取前需要先复位游标
            byte[] bytes = new byte[this.readBuf.remaining()];      //根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            this.readBuf.get(bytes);        //接收缓冲区数据
            String body = new String(bytes).trim();
            System.out.println("server:" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();      //获取服务端通道对象
        try {
            SocketChannel sc = ssc.accept();        //获取客户端通道对象
            sc.configureBlocking(false);        //设置为非阻塞
            sc.register(this.selector, SelectionKey.OP_READ);       //将通道注册到多路复用器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Demo3_Server(8765)).start();
    }
}
