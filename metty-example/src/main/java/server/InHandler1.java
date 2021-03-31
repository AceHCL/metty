package server;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-26 3:19 PM
 */
public class InHandler1 implements ChannelInBoundHandler {

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        ByteBuffer byteBuffer = (ByteBuffer) msg;
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String body = new String(bytes);
        ctx.fireChannelRead(body);
    }
}