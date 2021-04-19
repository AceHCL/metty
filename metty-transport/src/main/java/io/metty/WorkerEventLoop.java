package io.metty;

import io.heart.HeartHandler;
import io.heart.PingPong;
import io.heart.PongHandler;
import io.metty.channel.NioSocketChannel;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 11:06 PM
 */
public interface WorkerEventLoop extends NioEventLoop {
    void registerChannelTask(final NioSocketChannel nioSocketChannel, int opts, PingPong... pingPong);
}