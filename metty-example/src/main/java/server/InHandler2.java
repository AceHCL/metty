package server;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-29 3:57 PM
 */
public class InHandler2 implements ChannelInBoundHandler {

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        System.out.println(msg+"------"+"end");
        ByteBuffer byteBuffer = ByteBuffer.wrap("received".getBytes());
        byteBuffer.flip();
        ctx.write(byteBuffer);
    }
}