package io.metty.codec.encoder;

import io.metty.codec.MessageToByteEncoder;
import io.metty.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-01 8:45 下午
 */
public class LineFieldFrameEncoder extends MessageToByteEncoder {

    private final Logger logger = LoggerFactory.getLogger(LineFieldFrameEncoder.class);

    private final int lengthFieldLength;

    public LineFieldFrameEncoder() {
        this.lengthFieldLength = 4;
    }

    @Override
    protected ByteBuffer allocateByteBuffer(ByteBuffer buffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(lengthFieldLength+buffer.limit());
        return newBuffer;
    }

    @Override
    protected void encode(ByteBuffer newBuffer, ByteBuffer oldBuffer) {
        //添加lengthfield byte，表示大小
        int length = oldBuffer.limit();
        doEncode(newBuffer,oldBuffer,length);
    }

    private void doEncode(ByteBuffer newBuffer, ByteBuffer oldBuffer, int contentLength) {
        if (contentLength > (1<<30)){
            logger.error("contengLength 大于 表示的最大数");
        }
        //需要发送的消息体有多少个字节 contentLength
        //lengthField表示消息长度的值需要的字节数
        byte[] bytes = Utils.intToByteArray(contentLength);
        newBuffer.put(bytes);
        newBuffer.put(oldBuffer);
        newBuffer.flip();
    }
}