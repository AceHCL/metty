package io.metty;

import io.metty.channel.Channel;
import io.metty.invoke.ChannelInboundInvoker;
import io.metty.invoke.ChannelOutboundInvoker;
import io.metty.channel.NioSocketChannel;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 11:48 AM
 */
public interface ChannelContext extends ChannelInboundInvoker, ChannelOutboundInvoker {

    ChannelHandler handler();

    Channel channel();

    ChannelPipeline pipeline();

    @Override
    ChannelContext fireChannelRead(Object msg);

    @Override
    ChannelContext fireChannelReadComplete();

    @Override
    ChannelContext firechannelActive();

    @Override
    ChannelContext read();

    @Override
    ChannelInboundInvoker fireExceptionCaught(Throwable cause);

    @Override
    ChannelContext flush();


}