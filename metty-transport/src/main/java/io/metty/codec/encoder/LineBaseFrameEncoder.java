package io.metty.codec.encoder;

import io.metty.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 5:22 下午
 */
public class LineBaseFrameEncoder extends MessageToByteEncoder {

    private final byte[] delimiter = "\n".getBytes();

    @Override
    protected void encode(ByteBuffer newBuffer, ByteBuffer oldBuffer) {
        newBuffer.put(delimiter);
        newBuffer.put(oldBuffer);
        newBuffer.flip();
    }

    @Override
    protected ByteBuffer allocateByteBuffer(ByteBuffer buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(delimiter.length+buffer.capacity());
        return byteBuffer;
    }
}