package io.bootstrap;

import io.heart.PongHandler;
import io.metty.channel.NioSocketChannel;
import io.heart.HeartHandler;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.eventloop.NioWorkerEventLoop;
import io.metty.NioEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-24 10:34 AM
 */
public class NioBootstrap extends AbstractNioBootStrap{
    private static final Logger logger = LoggerFactory.getLogger(NioBootstrap.class);
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService scheduledPongService = Executors.newSingleThreadScheduledExecutor();
    private final HeartHandler heartHandler = new HeartHandler();
    private final PongHandler pongHandler = new PongHandler();

    public NioBootstrap() {
        scheduledExecutor.scheduleAtFixedRate(heartHandler,5,3, TimeUnit.SECONDS);
        scheduledPongService.scheduleAtFixedRate(pongHandler,10,9,TimeUnit.SECONDS);
    }

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
        heartHandler.put(nioSocketChannel);
        pongHandler.set(nioSocketChannel);
        logger.info("add socketchannel to heartHandler",this);
    }
}