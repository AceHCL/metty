package io.metty.codec;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-31 5:24 PM
 */
public class Cumulator {

    public ByteBuffer cumulate(ByteBuffer cumulation, ByteBuffer data) {
        ByteBuffer newCumulation = ByteBuffer.allocate(cumulation.remaining()+data.remaining());
        newCumulation.put(cumulation);
        newCumulation.put(data);
        newCumulation.flip();
        return newCumulation;
    }
}