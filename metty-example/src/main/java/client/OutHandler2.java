package client;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandler;

import java.nio.channels.SelectionKey;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-26 3:20 PM
 */
public class OutHandler2 implements ChannelOutBoundHandler {

    @Override
    public void channelActive(ChannelContext ctx) throws Exception {
        String msg = "";
        for (int i = 0; i < 100 ; i++) {
            msg = "fjaoejfioaefoiaeogoaigodafognodfnogdnsfognaodfngodfnogsfodgnosdfgno";
            ctx.write(msg);
        }
        ctx.channel().register(SelectionKey.OP_READ);
    }

}