package io.metty.codec;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandler;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-01 1:35 PM
 */
public abstract class MessageToByteEncoder implements ChannelOutBoundByteHandler, ChannelOutBoundHandler {
    @Override
    public void write(ChannelContext ctx, Object msg) throws Exception {
        //msg 转成 二进制，添加lemgthfield ，然后发送
        Objects.requireNonNull(msg,"msg is null");
        if (msg instanceof ByteBuffer){
            ByteBuffer buf = null;
            ByteBuffer buffer = (ByteBuffer) msg;
            buf = allocateByteBuffer(buffer);
            encode(buf,buffer);
            if (buf.hasRemaining()){
                ctx.write(buf);
            }else{
                buf = null;
                ctx.write(ByteBuffer.allocate(0));
            }
        }else{
            ctx.write(msg);
        }
    }

    protected abstract void encode(ByteBuffer newBuffer, ByteBuffer oldBuffer);

    protected abstract ByteBuffer allocateByteBuffer(ByteBuffer buffer);
}