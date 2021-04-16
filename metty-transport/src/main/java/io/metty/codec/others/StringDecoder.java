package io.metty.codec.others;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandlerAdapter;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 3:57 下午
 *
 *
 *
 *
 *
 */
public class StringDecoder extends ChannelInBoundHandlerAdapter {
    private  String character = "UTF-8";

    public StringDecoder() {
    }

    public StringDecoder(String character) {
        this.character = character==null?"UTF-8":character;
    }

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        ByteBuffer receivedMsg = (ByteBuffer) msg;
        byte[] bytes = new byte[receivedMsg.remaining()];
        receivedMsg.get(bytes);
        String stringMsg = new String(bytes,character);
        ctx.fireChannelRead(stringMsg);
    }
}