package server;

import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandler;
import util.Util;

import java.nio.ByteBuffer;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-29 3:57 PM
 */
public class InHandler2 implements ChannelInBoundHandler {

    private int anInt;
    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        String newmsg = Util.a;
        System.out.println(msg.equals(newmsg)+"-----" +anInt++);
    }
}