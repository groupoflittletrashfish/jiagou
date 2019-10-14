package com.noname.demo.demo5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by noname on 2019/10/9.
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        //创建两个事件循环器
        //用来接收N个客户端的连接处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //该线程组是对于每个客户端的具体实现，即业务实现
        EventLoopGroup workGroup = new NioEventLoopGroup();
        //辅助类，用来给Server端配置
        ServerBootstrap b = new ServerBootstrap();
        //指定线程组，并指定使用哪种类型的通道，此处使用的是NioServerSocketChannel，该通道即是TCP协议，若需要使用
        //HTTP或者UDP等协议，需要更换通道的类型
        b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class);
        //此处可以链式编程，此处方便注释，一定要使用childHandler去绑定事件处理器
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ServerHandler());      //在管道中添加自己定义的Handler
            }
        });
        /*
        SO_BACKLOG涉及到tcp协议的底层，较为复杂，简单描述下:
            TCP内核中有两个队列，简称为A和B
            1. 客户端发起connect时，会发送带有SYN的包（第一次握手）
            2. 服务端收到带有SYN的包，向客户端发送SYN ACK确认（第二次握手）
            3. 此时TCP内核将客户端放入到A队列中，服务器收到客户端发来的ACK确认（第三次握手）
            4. 此时TCP内核将客户端移入到B队列，连接完成，应用程序的accept将会返回，而accept操作就是从B队列取出已经完成的连接
            5. A队列和B队列的长度之和就是backlog,当A.B总和大于backlog时，新的连接将会被拒绝
            6. 所以若backlog值过小，可能会出现accept速度跟不上，A,B队列满了导致新的连接无法连接
            7. 注意的是，backlog的值对已经连接的连接无影响，只是对新的连接

        option()和childOption()这两个方法分别对应的是SocketChannel和子类的channel
        */
        b.option(ChannelOption.SO_BACKLOG, 128).option(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口，sync()是指异步监听
        ChannelFuture f = b.bind(8765).sync();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
