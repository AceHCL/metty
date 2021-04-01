package io.metty.codec.decoder;

import io.metty.ChannelContext;
import io.metty.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-30 6:15 PM
 */
public class LineBasedFrameDecoder extends ByteToMessageDecoder {
    private final Logger logger = LoggerFactory.getLogger(LineBasedFrameDecoder.class);
    private final String DEFAULTDELIMITER1 = "\n";
    private final String DEFAULTDELIMITER2 = "\t";
    private final DelimiterBasedFrameDecoder delimiterBasedFrameDecoder = new DelimiterBasedFrameDecoder(65535,DEFAULTDELIMITER1,DEFAULTDELIMITER2);
    @Override
    protected void decode(ChannelContext ctx, ByteBuffer in, List<Object> out) {
        delimiterBasedFrameDecoder.decode(ctx,in,out);
    }
}