package io.bootstrap;

import io.metty.ChannelHandler;
import io.metty.channel.Channel;
import io.metty.eventloop.NioEventLoopGroup;
import io.metty.NioEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 10:39 PM
 */
public abstract class AbstractNioBootStrap implements Bootstrap{

    private final Logger logger = LoggerFactory.getLogger(AbstractNioBootStrap.class);

    protected NioEventLoopGroup nioBossEventLoopGroup;


    protected InetSocketAddress inetSocketAddress;

    private ChannelHandler channelHandler;

    protected AbstractNioBootStrap.ChannelFactory factory;

    public interface ChannelFactory{
        Channel newChannel();
    }

    private final class DefaultChannelFactory implements ChannelFactory{
        private final Class<? extends Channel> clazz;

        public DefaultChannelFactory(Class<? extends Channel> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Channel newChannel() {
            Channel result = null;
            try {
                Objects.requireNonNull(clazz,"clazz not null");
                result = clazz.newInstance();
                logger.info("factory new channel success ");
            } catch (IllegalAccessException e) {
                logger.error("new channel error");
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public Bootstrap channel(Class<? extends Channel> channelClass){
        Objects.requireNonNull(channelClass,"clazz is null");
        this.factory = new DefaultChannelFactory(channelClass);
        return this;
    }


    @Override
    public Bootstrap childHandler(ChannelHandler channelHandler){
        this.channelHandler = channelHandler;
        return this;
    }

    @Override
    public Bootstrap bind(String hostName, int port) {
        this.inetSocketAddress = hostName == null?new InetSocketAddress(port):new InetSocketAddress(hostName,port);
        return this;
    }

    @Override
    public Bootstrap bind(int port) {
        return this.bind(null,port);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    @Override
    public NioEventLoop nextBossEventLoop() {
        return nioBossEventLoopGroup.next();
    }

}