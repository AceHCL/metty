package example0.server;

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
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String body = new String(bytes);
        System.out.println(body);
        byteBuffer.clear();
        byteBuffer.put(bytes);
        byteBuffer.flip();
        ctx.write(byteBuffer);
    }
}