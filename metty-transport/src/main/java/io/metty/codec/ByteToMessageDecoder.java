package io.metty.codec;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-31 4:37 PM
 */
public abstract class ByteToMessageDecoder implements ChannelInBoundByteHandler,ChannelInBoundHandler {

    ByteBuffer cumulation;
//    private Cumulator cumulator = MERGE_CUMULATOR;

    private boolean singleDecode;
    private boolean decodeWasNull;

    private boolean first;
    private int discardAfterReads = 16;
    private int numReads;

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuffer){
//            CodecOutputList out = CodecOutputList.newInstance();
            ByteBuffer data = (ByteBuffer) msg;
            first = cumulation == null;
            if (first){
                cumulation = data;
            }else{
//                cumulation =
            }
        }
    }
}