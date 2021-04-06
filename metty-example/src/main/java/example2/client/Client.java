package example2.client;

import io.bootstrap.NioBootstrap;
import io.metty.channel.NioSocketChannel;
import io.metty.codec.others.StringEncoder;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoopGroup;
import io.metty.handler.ChannelInitializer;

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

        nioBootstrap.group(nioEventLoopGroup).bind("127.0.0.1",8081).channel(NioSocketChannel.class).
                childHandler(new ChannelInitializer<NioSocketChannel>() {

            @Override
            public void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new StringEncoder("UTF-8"));
                nioSocketChannel.pipeline().addLast(new OutHandler());
            }
        }).start();
    }

}