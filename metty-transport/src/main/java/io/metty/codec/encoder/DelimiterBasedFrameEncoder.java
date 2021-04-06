package io.metty.codec.encoder;

import io.metty.codec.MessageToByteEncoder;
import io.metty.codec.decoder.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 5:28 下午
 */
public class DelimiterBasedFrameEncoder extends MessageToByteEncoder {

    private final Logger logger = LoggerFactory.getLogger(DelimiterBasedFrameDecoder.class);
    private final ByteBuffer delimiter;

    public DelimiterBasedFrameEncoder(String delimiter) {
        this.delimiter = init(delimiter);
    }

    private ByteBuffer init(String delimiter) {
        Objects.requireNonNull(delimiter);
        logger.info(delimiter);
        byte[] bytes = delimiter.getBytes();
        ByteBuffer result = ByteBuffer.allocate(bytes.length);
        result.put(bytes);
        result.flip();
        return result;
    }

    @Override
    protected void encode(ByteBuffer newBuffer, ByteBuffer oldBuffer) {
        newBuffer.put(delimiter);
        newBuffer.put(oldBuffer);
        newBuffer.flip();
    }

    @Override
    protected ByteBuffer allocateByteBuffer(ByteBuffer buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(delimiter.capacity()+buffer.capacity());
        return byteBuffer;
    }
}