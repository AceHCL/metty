package io.metty;

import io.metty.channel.NioServerSocketChannel;

import java.nio.channels.Selector;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:58 PM
 */
public interface BossEventLoop extends NioEventLoop {
    void registerChannelTask(final NioServerSocketChannel nioServerSocketChannel);
}