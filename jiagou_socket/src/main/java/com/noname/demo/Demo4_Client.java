package com.noname.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Created by noname on 2019/9/30.
 */
public class Demo4_Client implements Runnable {
    private AsynchronousSocketChannel asc;

    public Demo4_Client() throws IOException {
        asc = AsynchronousSocketChannel.open();
    }

    public void connect() {
        asc.connect(new InetSocketAddress("127.0.0.1", 8765));
    }

    public void write(String request) {
        try {
            asc.write(ByteBuffer.wrap(request.getBytes())).get();
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     */
    private void read() {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            asc.read(buf).get();
            buf.flip();
            byte[] respByte = new byte[buf.remaining()];
            buf.get(respByte);
            System.out.println(new String(respByte, "utf-8").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 此处暂时未做实现
     */
    @Override
    public void run() {
        while (true) {

        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Demo4_Client client1 = new Demo4_Client();
        Demo4_Client client2 = new Demo4_Client();
        Demo4_Client client3 = new Demo4_Client();

        client1.connect();
        client2.connect();
        client3.connect();

        new Thread(client1, "c1");
        new Thread(client2, "c2");
        new Thread(client3, "c3");

        Thread.sleep(1000);

        client1.write("c1 aaa");
        client2.write("c2 bbbb");
        client3.write("c3 ccccc");
    }
}
