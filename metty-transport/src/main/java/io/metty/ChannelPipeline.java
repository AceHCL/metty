package io.metty;

import io.metty.channel.Channel;
import io.metty.invoke.ChannelInboundInvoker;
import io.metty.invoke.ChannelOutboundInvoker;

import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 11:47 AM
 */
public interface ChannelPipeline extends ChannelInboundInvoker, ChannelOutboundInvoker {

    void addLast(ChannelHandler channelHandler);

    void addLasts(List<ChannelHandler> handlers);

    void addFirst(ChannelHandler channelHandler);

    Channel channel();

    @Override
    ChannelPipeline flush();

    @Override
    ChannelPipeline fireChannelRead(Object msg);

    @Override
    ChannelPipeline fireChannelReadComplete();

    @Override
    ChannelPipeline fireExceptionCaught(Throwable cause);

    @Override
    ChannelPipeline firechannelActive();
}