package io.heart;

import io.metty.channel.NioSocketChannel;
import io.metty.eventloop.NioWorkerEventLoop;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-16 11:48 上午
 */
public class HeartHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);
    private byte[] ping = "ping&".getBytes();
    private final ConcurrentLinkedQueue<NioSocketChannel> keySet = new ConcurrentLinkedQueue<>();


    public void put(NioSocketChannel nioSocketChannel){
        keySet.add(nioSocketChannel);
    }

    @Override
    public void run() {
        logger.info("------ping",this);
        for (NioSocketChannel nioSocketChannel : keySet) {
            NioWorkerEventLoop nioWorkerEventLoop = (NioWorkerEventLoop) nioSocketChannel.nioEventLoop;
            if (nioWorkerEventLoop == null){
                continue;
            }
            ByteBuffer pingBuffer = ByteBuffer.wrap(ping);
            nioWorkerEventLoop.registerScheduleTask(nioSocketChannel,pingBuffer);
        }
    }
}