package io.heart;

import io.metty.channel.NioSocketChannel;
import io.metty.eventloop.NioWorkerEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-16 11:48 上午
 */
public class HeartHandler implements Runnable,PingPong {
    private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);
    private byte[] ping = "ping&".getBytes();
    private final ConcurrentLinkedQueue<NioSocketChannel> keySet = new ConcurrentLinkedQueue<>();


    public void put(NioSocketChannel nioSocketChannel){
        keySet.add(nioSocketChannel);
    }

    @Override
    public void run() {
        if (keySet.isEmpty()){
            logger.info("no server connected");
            return;
        }
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

    @Override
    public void registerChannel(NioSocketChannel nioSocketChannel) {
        this.keySet.add(nioSocketChannel);
    }
}