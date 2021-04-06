package io.bootstrap;

import io.metty.ChannelHandler;
import io.metty.NioEventLoop;
import io.metty.channel.Channel;

import java.util.List;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:38 PM
 */
public interface Bootstrap {

    Bootstrap childHandler(ChannelHandler channelHandler);

    void start();

    Bootstrap bind(String hostName, int port);

    Bootstrap bind(int port);

    default ChannelHandler getChannelHandler(){
        return null;
    }

    default NioEventLoop nextWorkerEventLoop(){return null;}

    default NioEventLoop nextBossEventLoop() {return null;}

    Bootstrap channel(Class<? extends Channel> channelClass);
}