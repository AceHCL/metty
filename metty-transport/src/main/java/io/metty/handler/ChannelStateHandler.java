package io.metty.handler;

import io.metty.ChannelContext;
import io.metty.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 3:05 下午
 */
public interface ChannelStateHandler extends ChannelHandler {

    @Override
    void channelRegistered(ChannelContext var1) throws Exception;

}