package io.metty.handler;

import io.metty.ChannelContext;
import io.metty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 3:00 下午
 */
public abstract class ChannelInitializer<C extends Channel>  extends ChannelStateHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInitializer.class);

    public abstract void initChannel(C var1) throws Exception;

    @Override
    public void channelRegistered(ChannelContext ctx) throws Exception {
        this.initChannel((C) ctx.channel());
        ctx.pipeline().remove(this);
    }
}