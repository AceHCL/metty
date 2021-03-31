package io.metty.channel;

import com.sun.webkit.EventLoop;
import io.metty.ChannelFuture;
import io.metty.ChannelPipeline;
import io.metty.NioEventLoop;
import io.metty.pipeline.DefaultChannelPipeline;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-16 9:12 PM
 */
public abstract class AbstractChannel implements Channel {

    private final ChannelPipeline pipeline;
    private final Unsafe unsafe;
    private volatile SocketAddress remoteAddress;
    private volatile SocketAddress localAddress;
    protected   NioEventLoop nioEventLoop;
    public void bindEventLoop(NioEventLoop nioEventLoop){
        this.nioEventLoop = nioEventLoop;
    }

    protected AbstractChannel() {
        this.pipeline = newChannelPipeline();
        this.unsafe = newUnsafe();
    }

    protected ChannelPipeline newChannelPipeline() {
        return new DefaultChannelPipeline(this);
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public Channel flush() {
        pipeline.flush();
        return this;
    }

    public Channel channelActive(){
        pipeline.firechannelActive();
        return this;
    }





    @Override
    public Channel read() {
        pipeline.read();
        return this;
    }

    @Override
    public ChannelFuture write(Object msg) {
        return pipeline.write(msg);
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return pipeline.writeAndFlush(msg);
    }

    @Override
    public Unsafe unsafe() {
        return unsafe;
    }

    @Override
    public SocketAddress remoteAddress() {
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            try {
                this.remoteAddress = remoteAddress = unsafe().remoteAddress();
            } catch (Error e) {
                throw e;
            } catch (Throwable t) {
                // Sometimes fails on a closed socket in Windows.
                return null;
            }
        }
        return remoteAddress;
    }

    @Override
    public SocketAddress localAddress() {
        SocketAddress localAddress = this.localAddress;
        if (localAddress == null){
            this.localAddress = localAddress = unsafe().localAddress();
        }
        return localAddress;
    }

    public ChannelFuture bind(){
        return this.bind(null);
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        this.localAddress = localAddress;
        return pipeline.bind(localAddress);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return pipeline.connect(remoteAddress);
    }

    @Override
    public ChannelFuture disconnect() {
        return pipeline.disconnect();
    }

    @Override
    public ChannelFuture close() {
        return pipeline.close();
    }

    private   SocketAddress remoteAddress0(){
        return new InetSocketAddress("127.0.0.1",8080);
    }

    private   SocketAddress localAddress0(){
        return new InetSocketAddress("127.0.0.1",8080);
    }

    protected abstract void doBind(SocketAddress localAddress) throws Exception;

    protected abstract void doConnect(SocketAddress remoteAddress) throws Exception;

    protected abstract void doBeginRead() throws Exception;

    protected abstract AbstractUnsafe newUnsafe();



    protected abstract class AbstractUnsafe implements Unsafe {

        @Override
        public SocketAddress remoteAddress() {
            return remoteAddress0();
        }

        @Override
        public SocketAddress localAddress() {
            return localAddress0();
        }

        @Override
        public void bind(SocketAddress localAddress) {
            try {
                doBind(localAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void connect(SocketAddress remoteAddress) {
            try {
                doConnect(remoteAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void beginRead() {
            try {
                doBeginRead();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}