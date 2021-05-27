package io.metty.seriliazble;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-05-27 11:40 上午
 */
public class ArrayToObjectHandler implements ChannelInBoundHandler {

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        ByteBuffer byteBuffer = (ByteBuffer) msg;
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        Object result = Coder.byteArrayToObject(bytes);
        ctx.fireChannelRead(result);
    }
}