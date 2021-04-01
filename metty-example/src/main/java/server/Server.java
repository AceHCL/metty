package server;

import io.bootstrap.NioServerBootstrap;
import io.metty.ChannelHandler;
import io.metty.codec.decoder.DelimiterBasedFrameDecoder;
import io.metty.codec.decoder.LineBasedFrameDecoder;
import io.metty.eventloop.NioBossEventLoopGroup;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoopGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 11:44 PM
 */
public class Server {

    public static void main(String[] args) {

        NioServerBootstrap serverBootstrap = new NioServerBootstrap();
        NioEventLoopGroup bossGroup = new NioBossEventLoopGroup(1);
        NioEventLoopGroup workkerGroup = new NioWorkerEventLoopGroup(1);
        ((NioBossEventLoopGroup) bossGroup).setBootstrap(serverBootstrap);
        ((NioWorkerEventLoopGroup) workkerGroup).setBootstrap(serverBootstrap);

        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new LineBasedFrameDecoder());
        channelHandlers.add(new InHandler1());
        channelHandlers.add(new InHandler2());
        serverBootstrap.group(bossGroup,workkerGroup).bind(8081).childHandler(channelHandlers).start();

    }


}