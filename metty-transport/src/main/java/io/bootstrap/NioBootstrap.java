package io.bootstrap;

import io.metty.channel.NioSocketChannel;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoop;
import io.metty.NioEventLoop;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-24 10:34 AM
 */
public class NioBootstrap extends AbstractNioBootStrap{



    @Override
    public NioEventLoop nextWorkerEventLoop() {
        return this.nextBossEventLoop();
    }

    public NioBootstrap group(NioEventLoopGroup nioEventLoopGroup){
        this.nioBossEventLoopGroup  = nioEventLoopGroup;
        return this;
    }

    public InetSocketAddress getInetSocketAddress(){
        return this.inetSocketAddress;
    }

    @Override
    public void start() {
        NioSocketChannel nioSocketChannel = (NioSocketChannel) factory.newChannel();
        try {
            nioSocketChannel.connect(inetSocketAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //addHandlers
        nioSocketChannel.pipeline().addLast(this.getChannelHandler());
        NioWorkerEventLoop nioWorkerEventLoop = (NioWorkerEventLoop) this.nextWorkerEventLoop();
        nioSocketChannel.bindEventLoop(nioWorkerEventLoop);
        nioWorkerEventLoop.mapPut(nioSocketChannel.getSocketChannel(),nioSocketChannel);
        nioWorkerEventLoop.registerChannelTask(nioSocketChannel, SelectionKey.OP_CONNECT);

    }
}