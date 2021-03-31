package io.metty.channel;

import com.sun.webkit.EventLoop;
import io.metty.ChannelPipeline;
import io.metty.NioEventLoop;
import io.metty.invoke.ChannelOutboundInvoker;

import java.net.SocketAddress;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-16 8:53 PM
 */
public interface Channel extends ChannelOutboundInvoker {

    Unsafe unsafe();

    ChannelPipeline pipeline();
    @Override
    Channel read();

    @Override
    Channel flush();

    SocketAddress remoteAddress();

    SocketAddress localAddress();

    void register(int opts);

    interface Unsafe {

        void bind(SocketAddress localAddress);

        void connect(SocketAddress remoteAddress);

        void beginRead();

        void write(Object msg);

        void flush();

        SocketAddress remoteAddress();

        SocketAddress localAddress();
    }
}