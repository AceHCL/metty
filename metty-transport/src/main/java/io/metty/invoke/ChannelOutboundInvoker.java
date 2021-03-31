package io.metty.invoke;

import io.metty.ChannelFuture;

import java.net.SocketAddress;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-16 5:56 PM
 */
public interface ChannelOutboundInvoker {

    ChannelOutboundInvoker read();

    ChannelFuture write(Object msg);

    ChannelOutboundInvoker flush();

    ChannelFuture writeAndFlush(Object msg);

    ChannelFuture bind(SocketAddress localAddress);

    ChannelFuture connect(SocketAddress remoteAddress);

    ChannelFuture disconnect();

    ChannelFuture close();

}