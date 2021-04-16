package io.metty.eventloop;

import io.bootstrap.NioServerBootstrap;
import io.metty.BossEventLoop;
import io.metty.channel.NioServerSocketChannel;
import io.metty.channel.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 11:03 PM
 */
public class NioBossEventLoop extends AbstractNioEventLoop implements BossEventLoop {

    private static final Logger log = LoggerFactory.getLogger(NioBossEventLoop.class);

    public NioBossEventLoop(AbstractNioEventLoopGroup parent, Executor executor, String threadName) {
        super(parent, executor, threadName);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    @Override
    protected void process(Selector selector) throws IOException {


        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()){
            return;
        }
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                NioServerSocketChannel nioServerSocketChannel = (NioServerSocketChannel) this.map.get(serverSocketChannel);
                //新客户端
                NioSocketChannel nioSocketChannel = nioServerSocketChannel.accept();
                NioServerBootstrap nioServerBootstrap = (NioServerBootstrap) parent.getBootstrap();
                log.info("finishConnected",nioSocketChannel);
                nioSocketChannel.pipeline().addLast(nioServerBootstrap.getChannelHandler());
                nioSocketChannel.channelRegistered();
                NioWorkerEventLoop nioWorkerEventLoop = (NioWorkerEventLoop) nioServerBootstrap.nextWorkerEventLoop();
                nioSocketChannel.bindEventLoop(nioWorkerEventLoop);
                nioServerBootstrap.pingHandler.set(nioSocketChannel);
                nioWorkerEventLoop.map.put(nioSocketChannel.getSocketChannel(),nioSocketChannel);
                //注册新客户端接入任务
                nioWorkerEventLoop.registerChannelTask(nioSocketChannel,SelectionKey.OP_READ);
            }
        }

    }

    @Override
    public void registerChannelTask(NioServerSocketChannel nioServerSocketChannel) {
        log.info("register example1.server channel");
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocketChannel serverSocketChannel = nioServerSocketChannel.getServerSockeetChannel();
                    serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Selector getSelector() {
        return selector;
    }
}