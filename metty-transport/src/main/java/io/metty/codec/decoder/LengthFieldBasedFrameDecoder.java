package io.metty.codec.decoder;

import io.metty.ChannelContext;
import io.metty.codec.ByteToMessageDecoder;
import io.metty.codec.encoder.LineFieldFrameEncoder;
import io.metty.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-31 4:05 PM
 */
public class LengthFieldBasedFrameDecoder extends ByteToMessageDecoder {

    private final Logger logger = LoggerFactory.getLogger(LineFieldFrameEncoder.class);

    private final int lengthFieldLength;

    public LengthFieldBasedFrameDecoder() {
        this.lengthFieldLength = 4;
    }

    @Override
    protected void decode(ChannelContext ctx, ByteBuffer in, List<Object> out) {
        Object decoded = decode(ctx,in);
        while (decoded != null){
            out.add(decoded);
            decoded = decode(ctx,in);
        }
    }

    private Object decode(ChannelContext ctx, ByteBuffer in) {
        if (in.remaining() >= lengthFieldLength){
            byte[] lengthBytes = new byte[lengthFieldLength];
            int position = in.position();
            in.mark();
            in.get(lengthBytes,position,lengthFieldLength);
            int contentLength = Utils.byteArrayToInt(lengthBytes);
            if (contentLength <= in.remaining()){
                byte[] contentBytes = new byte[contentLength];
                in.get(contentBytes,0,contentLength);
                ByteBuffer result = ByteBuffer.allocate(contentLength);
                result.put(contentBytes);
                result.flip();
                return result;
            }
            in.reset();
        }
        return null;
    }
}