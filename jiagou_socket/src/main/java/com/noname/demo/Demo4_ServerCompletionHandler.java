package com.noname.demo;


import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * Created by noname on 2019/9/26.
 * <p>
 * CompletionHandler接口的泛型中第二个参数指的是服务器端的对象
 */
public class Demo4_ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Demo4_Server> {
    /**
     * 连接成功后将会执行此方法
     *
     * @param asc
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel asc, Demo4_Server attachment) {
        //此方法就是Demo4_Server中的accept(),这么做是为了连接成功后再次阻塞，直到下一个客户端进行连接
        attachment.assc.accept(attachment, this);
        read(asc);
    }

    /**
     * 读取数据
     *
     * @param asc 客户端的通道
     */
    private void read(AsynchronousSocketChannel asc) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        asc.read(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                System.out.println("server->" + "收到客户端的数据长度为:" + result);
                String resultData = new String(attachment.array()).trim();
                System.out.println("server->" + "收到客户端的信息为:" + resultData);
                String response = "收到客户端的信息为:" + resultData;
                write(asc, response);       //将收到的信息返回给客户端
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    /**
     * 连接失败后执行此方法
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, Demo4_Server attachment) {
        exc.printStackTrace();
    }

    private void write(AsynchronousSocketChannel asc, String response) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.put(response.getBytes());
            buf.flip();
            asc.write(buf).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
