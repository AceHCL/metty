package io.metty.eventloop;

import io.bootstrap.Bootstrap;
import io.bootstrap.NioBootstrap;
import io.metty.WorkerEventLoop;
import io.metty.channel.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 11:08 PM
 */
public class NioWorkerEventLoop extends AbstractNioEventLoop implements WorkerEventLoop {

    private static final Logger log = LoggerFactory.getLogger(NioWorkerEventLoop.class);

    public NioWorkerEventLoop(AbstractNioEventLoopGroup parent, Executor executor, String threadName) {
        super(parent, executor, threadName);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select(500);
    }

    @Override
    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()){
            return;
        }
        Iterator iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            NioSocketChannel nioSocketChannel = (NioSocketChannel) this.map.get(socketChannel);
            if (key.isConnectable()){
                log.info("socketchannel connected");
                if (socketChannel.finishConnect()){
                    nioSocketChannel.channelRegistered();
                    nioSocketChannel.channelActive();
                }
            }
            if (key.isReadable()){
                nioSocketChannel.read();
            }
        }
    }

    @Override
    public void registerChannelTask(final NioSocketChannel nioSocketChannel, int opts) {
        log.info("register  clientchannel");
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    nioSocketChannel.register(selector, opts);
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