package io.metty.codec;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-31 4:37 PM
 */
public abstract class ByteToMessageDecoder implements ChannelInBoundByteHandler,ChannelInBoundHandler {

    private final Logger logger = LoggerFactory.getLogger(ByteToMessageDecoder.class);
    ByteBuffer cumulation;
    private Cumulator cumulator;

    private boolean singleDecode = true;

    private boolean first;
    private int discardAfterReads = 16;
    private int numReads;

    protected ByteToMessageDecoder() {
        this.cumulator = new Cumulator();
    }

    public void setSingleDecode(boolean singleDecode) {
        this.singleDecode = singleDecode;
    }

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        Objects.requireNonNull(msg,"msg is null");
        if (msg instanceof ByteBuffer){
            List<Object> out = new ArrayList<>();
            try {
                //already flip();
                ByteBuffer data = (ByteBuffer) msg;
                first = cumulation == null;
                if (first){
                    cumulation = data;
                }else{
                    cumulation = cumulator.cumulate(cumulation,data);
                }
                callDecode(ctx,cumulation,out);
            }catch (Exception e){
                logger.error("decode error");
                e.printStackTrace();
            }catch (Throwable t){
                throw new Exception(t);
            }finally {
                if (cumulation != null && !cumulation.hasRemaining()){
                    numReads = 0;
                    cumulation = null;
                }else if (++numReads >= discardAfterReads){
                    numReads = 0;
                    discardSomeReadBytes();
                }
            }
            int size = out.size();
            fireChannelRead(ctx,out,size);
            out.clear();
        }else{
            logger.info("msg not instance of bytebuffer");
            ctx.fireChannelRead(msg);
        }
    }

    private void discardSomeReadBytes() {
        if (cumulation != null && !first){
            //to to
        }
    }

    private void callDecode(ChannelContext ctx, ByteBuffer in, List<Object> out) {
        try {
            while (in.hasRemaining()){
                int outSize = out.size();
                if (outSize > 0){
                    fireChannelRead(ctx,out,outSize);
                    out.clear();
                    outSize = 0;
                }
                int oldInputLength = in.remaining();
                decode(ctx,in,out);
                if (outSize == out.size()){
                    if (oldInputLength == in.remaining()){
                        break;
                    }else {
                        continue;
                    }
                }
                if (oldInputLength == in.remaining()){

                }
                if (isSingleDecode()){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fireChannelRead(ChannelContext ctx, List<Object> out, int outSize) {
        for (Object msg: out
             ) {
            ctx.fireChannelRead(msg);
        }
    }

    protected List<ByteBuffer> init(String[] delimiters) {
        logger.info("init delimiters");
        List<ByteBuffer> result = new ArrayList<>();
        for (String limiter: delimiters
        ) {
            try {
                byte[] bytes = limiter.getBytes("UTF-8");
                ByteBuffer delimiter = ByteBuffer.allocate(bytes.length);
                delimiter.put(bytes);
                delimiter.flip();
                result.add(delimiter);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean isSingleDecode() {
        return  singleDecode;
    }

    protected abstract void decode(ChannelContext ctx, ByteBuffer in, List<Object> out);
}