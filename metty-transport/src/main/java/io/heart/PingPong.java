package io.heart;

import io.metty.channel.NioSocketChannel;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-17 12:54 下午
 */
public interface PingPong {
    void registerChannel(NioSocketChannel nioSocketChannel);
}