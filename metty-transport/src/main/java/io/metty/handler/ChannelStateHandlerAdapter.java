package io.metty.handler;

import io.metty.ChannelContext;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 3:06 下午
 */
public class ChannelStateHandlerAdapter implements ChannelStateHandler {

    @Override
    public void channelRegistered(ChannelContext ctx) throws Exception {
        ctx.firechannelRegistered();
    }
}