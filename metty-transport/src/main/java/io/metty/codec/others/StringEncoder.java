package io.metty.codec.others;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandlerAdpter;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 3:57 下午
 */
public class StringEncoder  extends ChannelOutBoundHandlerAdpter {

    private String character = "UTF-8";

    public StringEncoder() {
    }

    public StringEncoder(String character) {
        this.character = character==null?"UTF-8":character;
    }

    @Override
    public void write(ChannelContext ctx, Object msg) throws Exception {
        String sendMsg = (String) msg;
        byte[] bytes = ((String) msg).getBytes();
        ByteBuffer sendBuffer = ByteBuffer.allocate(bytes.length);
        sendBuffer.put(bytes);
        sendBuffer.flip();
        ctx.write(sendBuffer);
    }
}