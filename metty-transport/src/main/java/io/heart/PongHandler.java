package io.heart;

import io.metty.channel.NioSocketChannel;
import io.metty.eventloop.NioWorkerEventLoop;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-16 5:28 下午
 */
public class PongHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);

    private final ConcurrentLinkedQueue<NioSocketChannel> keySet = new ConcurrentLinkedQueue<>();

    public void set(NioSocketChannel nioSocketChannel){
        keySet.add(nioSocketChannel);
    }
    @Override
    public void run() {
        logger.info("--------pong,check connected success",this);
        for (NioSocketChannel nioSocketChannel : keySet) {
           if (nioSocketChannel.pongflag.get()){
               nioSocketChannel.pongflag.set(false);
           }else{
               try {
                   nioSocketChannel.getSocketChannel().close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               NioWorkerEventLoop nioWorkerEventLoop = (NioWorkerEventLoop) nioSocketChannel.nioEventLoop;
               nioWorkerEventLoop.registerChannelTask(nioSocketChannel, SelectionKey.OP_CONNECT);
           }
        }
    }
}