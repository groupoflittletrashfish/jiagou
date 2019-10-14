package com.noname.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by noname on 2019/9/26.
 * NIO-客户端
 */
public class Demo3_Client {

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);
        SocketChannel sc = null;
        ByteBuffer buf = ByteBuffer.allocate(1024);

        try {
            sc = SocketChannel.open();      //打开通道
            sc.connect(address);            //进行连接
            while (true) {
                byte[] bytes = new byte[1024];      //定义字节数组
                System.in.read(bytes);              //键盘输入

                buf.put(bytes);                     //将输入的数据刷入缓冲区
                buf.flip();                         //此时游标一定是在最后的，需要复位
                sc.write(buf);
                buf.clear();                        //清除数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
