package client;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandler;
import util.Util;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-26 3:20 PM
 */
public class OutHandler2 implements ChannelOutBoundHandler {

    @Override
    public void channelActive(ChannelContext ctx) throws Exception {
        String msg = Util.a;
        for (int i = 0; i < 1 ; i++) {
            ctx.write(msg+"\n");
        }
    }

}