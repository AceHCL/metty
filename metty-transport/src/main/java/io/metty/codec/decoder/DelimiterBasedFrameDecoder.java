package io.metty.codec.decoder;

import com.sun.deploy.util.StringUtils;
import io.metty.ChannelContext;
import io.metty.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-30 6:17 PM
 */
public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {

    private final Logger logger = LoggerFactory.getLogger(DelimiterBasedFrameDecoder.class);
    private final List<ByteBuffer> delimiters;
    private final int maxFrameLength;

    public DelimiterBasedFrameDecoder(int maxFrameLength,String... delimiters) {
        this.maxFrameLength = maxFrameLength;
        this.delimiters = init(delimiters);
    }

    @Override
    protected void decode(ChannelContext ctx, ByteBuffer byteBuffer, List<Object> out) {
        Object decoded = decode(ctx,byteBuffer);
        while (decoded != null){
            out.add(decoded);
            decoded = decode(ctx,byteBuffer);
        }
    }

    private Object decode(ChannelContext ctx, ByteBuffer byteBuffer) {
        int minFrameLength = Integer.MAX_VALUE;
        ByteBuffer minDelimiter = null;
        for (ByteBuffer delimiter: delimiters
             ) {
            int frameLength = indexOf(byteBuffer,delimiter);
            if (frameLength > 0 && frameLength < minFrameLength ){
                minFrameLength = frameLength;
                minDelimiter = delimiter;
            }
        }
        if (minDelimiter != null){
            int minDelimiterLength = minDelimiter.capacity();
            ByteBuffer frame;

            if (minFrameLength > maxFrameLength){
                byteBuffer.position(minDelimiterLength+minFrameLength);
                logger.warn("minFameLength lager than maxFrameLength");
                return null;
            }
            byte[] bytes = new byte[minFrameLength];
            byteBuffer.get(bytes,0,bytes.length);
            byteBuffer.position(byteBuffer.position()+minDelimiterLength);
            frame = ByteBuffer.allocate(bytes.length);
            frame.put(bytes);
            frame.flip();
            return frame;
        }
        return null;
    }

    private int indexOf(ByteBuffer byteBuffer,ByteBuffer delimiter){
        int oldPosition = byteBuffer.position();
        byte[] a = new byte[byteBuffer.remaining()];
        byte[] b = new byte[delimiter.remaining()];
        byteBuffer.get(a);
        delimiter.get(b);
        byteBuffer.position(oldPosition);
        delimiter.flip();
        int index = 0;
        try {
            String aa = new String(a,"ISO-8859-1");
            String bb = new String(b,"ISO-8859-1");
            if (!aa.isEmpty()){
                index = aa.indexOf(bb);
                index = index < 0?0:index;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("delimiter indexOf byffer error ");
        }
        return index;
    }
}