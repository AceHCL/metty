package io.metty;

import javafx.beans.binding.ObjectExpression;

import java.net.SocketAddress;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 11:55 AM
 */
public interface ChannelHandler{

    default void handlerAdded(ChannelContext ctx) throws Exception{}

    default void handlerRemoved(ChannelContext ctx) throws Exception{}


    default void channelRead(ChannelContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    default void channelReadComplete(ChannelContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    default void exceptionCaught(ChannelContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    default void read(ChannelContext ctx) throws Exception {
        ctx.read();
    }

    default void write(ChannelContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }

    default void flush(ChannelContext ctx) throws Exception {
        ctx.flush();
    }
    default void connect(ChannelContext ctx, SocketAddress remoteAddress) throws Exception{
        ctx.connect(remoteAddress);
    }

    default void bind(ChannelContext ctx, SocketAddress localAddress) throws Exception{
        ctx.bind(localAddress);
    }

    default void channelActive(ChannelContext ctx) throws Exception{
        ctx.firechannelActive();
    }
}