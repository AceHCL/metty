package io.heart;

import io.metty.channel.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-16 5:13 下午
 */
public class PingHandler implements Runnable, PingPong {

    private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);


    private final ConcurrentLinkedQueue<NioSocketChannel> keySet = new ConcurrentLinkedQueue<>();
    public void set(NioSocketChannel nioSocketChannel){
        keySet.add(nioSocketChannel);
    }

    @Override
    public void run() {

        if (keySet.isEmpty()){
            logger.info("--------null client connected",this);
            return ;
        }
        logger.info("--------ping,check connected success",this);
        for (NioSocketChannel nioSocketChannel : keySet) {
            if (nioSocketChannel.pingflag.get()){
                nioSocketChannel.pongflag.set(false);
            }else{
                try {
                    keySet.remove(nioSocketChannel);
                    nioSocketChannel.getSocketChannel().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void registerChannel(NioSocketChannel nioSocketChannel) {
        this.keySet.add(nioSocketChannel);
    }
}