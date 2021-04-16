package io.bootstrap;

import io.heart.HeartHandler;
import io.heart.PingHandler;
import io.metty.channel.NioServerSocketChannel;
import io.metty.eventloop.*;
import io.metty.NioEventLoop;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:38 PM
 */
public class NioServerBootstrap extends AbstractNioBootStrap{

    private NioWorkerEventLoopGroup nioWorkerEventLoopGroup;
    private final ScheduledExecutorService schedulePingService = Executors.newSingleThreadScheduledExecutor();
    public final PingHandler pingHandler = new PingHandler();

    public NioServerBootstrap() {
        schedulePingService.scheduleAtFixedRate(pingHandler,10,9, TimeUnit.SECONDS);
    }

    @Override
    public void start(){
        for (int i = 0; i < 1 ; i++) {
            try {
                NioServerSocketChannel nioServerSocketChannel = (NioServerSocketChannel) factory.newChannel();
                nioServerSocketChannel.bind(inetSocketAddress);
                NioBossEventLoop nioBossEventLoop = (NioBossEventLoop) this.nextBossEventLoop();
                nioServerSocketChannel.bindEventLoop(nioBossEventLoop);
                nioBossEventLoop.mapPut(nioServerSocketChannel.getServerSockeetChannel(),nioServerSocketChannel);
                nioBossEventLoop.registerChannelTask(nioServerSocketChannel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NioEventLoop nextWorkerEventLoop(){
        return nioWorkerEventLoopGroup.next();
    }

    public NioServerBootstrap group(NioEventLoopGroup workerGroup){
        NioEventLoopGroup bossGroup = new NioBossEventLoopGroup();
        this.group(bossGroup,workerGroup);
        return this;
    }
    public NioServerBootstrap group(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup){
        this.nioBossEventLoopGroup  = bossGroup;
        this.nioWorkerEventLoopGroup = (NioWorkerEventLoopGroup) workerGroup;
        return this;
    }
}