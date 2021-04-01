package client;

import io.bootstrap.NioBootstrap;
import io.metty.ChannelHandler;
import io.metty.channel.Channel;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoopGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 10:55 AM
 */
public class Client {

    public static void main(String[] args) {

        NioEventLoopGroup nioEventLoopGroup = new NioWorkerEventLoopGroup(1);

        NioBootstrap nioBootstrap = new NioBootstrap();
        ((NioWorkerEventLoopGroup) nioEventLoopGroup).setBootstrap(nioBootstrap);

        List<ChannelHandler>  channelHandlers = new ArrayList<>();
        channelHandlers.add(new OutHandler1());
        channelHandlers.add(new OutHandler2());
        nioBootstrap.group(nioEventLoopGroup).bind("127.0.0.1",8081).childHandler(channelHandlers).start();
    }

}