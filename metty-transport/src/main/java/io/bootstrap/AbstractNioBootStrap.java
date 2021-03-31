package io.bootstrap;

import io.metty.ChannelHandler;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.NioEventLoop;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:39 PM
 */
public abstract class AbstractNioBootStrap implements Bootstrap{

    protected NioEventLoopGroup nioBossEventLoopGroup;


    protected InetSocketAddress inetSocketAddress;

    private List<ChannelHandler> channelHandlers;
    @Override
    public Bootstrap childHandler(List<ChannelHandler> channelHandlers){
        this.channelHandlers = channelHandlers;
        return this;
    }

    @Override
    public Bootstrap bind(String hostName, int port) {
        this.inetSocketAddress = hostName == null?new InetSocketAddress(port):new InetSocketAddress(hostName,port);
        return this;
    }

    @Override
    public Bootstrap bind(int port) {
        return this.bind(null,port);
    }

    @Override
    public List<ChannelHandler> getChannelHandlers(){
        return channelHandlers;
    }

    @Override
    public NioEventLoop nextBossEventLoop() {
        return nioBossEventLoopGroup.next();
    }

}