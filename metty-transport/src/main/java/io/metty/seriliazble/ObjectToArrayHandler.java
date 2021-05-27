package io.metty.seriliazble;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandler;
import io.metty.handler.ChannelOutBoundHandlerAdpter;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-05-27 11:39 上午
 */
public class ObjectToArrayHandler implements ChannelOutBoundHandler {

    @Override
    public void write(ChannelContext ctx, Object msg) throws Exception {
        byte[] result = Coder.objectToByteArray(msg);
        ByteBuffer byteBuffer = ByteBuffer.allocate(result.length);
        byteBuffer.put(result);
        byteBuffer.flip();
        ctx.write(byteBuffer);
    }
}