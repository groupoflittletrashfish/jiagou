package com.noname.demo.demo5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by noname on 2019/10/9.
 * <p>
 * 服务端对消息的处理，ChannelHandlerAdapter作为适配器继承了ChannelHandler接口，该示例只重写其中的两个方法
 */
public class ServerHandler extends ChannelHandlerAdapter {

    /**
     * 读取流失败时的处理逻辑
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();        //打印错误日志
        ctx.close();                    //关闭
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;         //此处强转成ByteBuf是因为client端发送的数据类型就是ByteBuf
            byte[] by = new byte[buf.readableBytes()];          //buf.readableBytes()该方法是获取有效的缓冲字节数
        } finally {
            ReferenceCountUtil.release(msg);        //释放资源
        }

    }
}
