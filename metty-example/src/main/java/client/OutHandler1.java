package client;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandler;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-26 3:20 PM
 */
public class OutHandler1 implements ChannelOutBoundHandler {

    @Override
    public void write(ChannelContext ctx, Object msg) throws Exception {
        String sendString = (String) msg;
        byte[] bytes = sendString.getBytes("UTF-8");
        ByteBuffer send = ByteBuffer.allocate(bytes.length);
        send.put(bytes);
        send.flip();
        ctx.write(send);
    }
}