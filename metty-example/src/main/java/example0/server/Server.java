package example0.server;

import io.bootstrap.NioServerBootstrap;
import io.metty.channel.NioServerSocketChannel;
import io.metty.channel.NioSocketChannel;
import io.metty.codec.decoder.LineBasedFrameDecoder;
import io.metty.eventloop.NioBossEventLoopGroup;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoopGroup;
import io.metty.handler.ChannelInitializer;

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
        serverBootstrap.group(bossGroup,workkerGroup).bind(8081).channel(NioServerSocketChannel.class)
    .childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            public void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new InHandler1());
            }
        }).start();

    }


}