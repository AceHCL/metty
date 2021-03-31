package io.metty.invoke;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-16 5:56 PM
 */
public interface ChannelInboundInvoker {

    ChannelInboundInvoker firechannelActive();

    ChannelInboundInvoker fireChannelRead(Object msg);

    ChannelInboundInvoker fireChannelReadComplete();

    ChannelInboundInvoker fireExceptionCaught(Throwable cause);

}