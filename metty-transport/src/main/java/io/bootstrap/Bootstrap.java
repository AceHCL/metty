package io.bootstrap;

import io.metty.ChannelHandler;
import io.metty.NioEventLoop;

import java.util.List;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:38 PM
 */
public interface Bootstrap {

    Bootstrap childHandler(List<ChannelHandler> channelHandlers);

    void start();

    Bootstrap bind(String hostName, int port);

    Bootstrap bind(int port);

    default List<ChannelHandler> getChannelHandlers(){
        return null;
    }

    default NioEventLoop nextWorkerEventLoop(){return null;}

    default NioEventLoop nextBossEventLoop() {return null;}
}