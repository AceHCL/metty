package example2.client;

import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandlerAdpter;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 4:10 下午
 */
public class OutHandler extends ChannelOutBoundHandlerAdpter {

    @Override
    public void channelActive(ChannelContext ctx) throws Exception {
        String send = "hello,world\n";
        ctx.write(send);
    }
}