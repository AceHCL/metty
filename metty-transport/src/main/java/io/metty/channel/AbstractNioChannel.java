package io.metty.channel;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 8:37 AM
 */
public abstract class AbstractNioChannel extends AbstractChannel {

    private final SelectableChannel channel;



    protected AbstractNioChannel(SelectableChannel channel) {
        super();
        this.channel = channel;
        try {
            channel.configureBlocking(false);
        } catch (IOException e) {
            try {
                channel.close();
            } catch (IOException e1) {
                //record log
            }
            e.printStackTrace();
        }
    }

    protected SelectableChannel javaChannel(){
        return channel;
    }

    @Override
    public void register(int opts) {
        try {
            channel.register(nioEventLoop.getSelector(),opts);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    protected abstract void doFinishConnect() throws Exception;


    @Override
    public NioUnsafe unsafe() {
        return (NioUnsafe) super.unsafe();
    }
    public interface NioUnsafe extends Unsafe {

        SelectableChannel ch();

        void finishConnect();

        void read();

        void forceFlush();
    }

    protected abstract class AbstractNioUnsafe extends AbstractUnsafe implements NioUnsafe {

        @Override
        public SelectableChannel ch() {
            return javaChannel();
        }

        @Override
        public void finishConnect() {
            try {
                doFinishConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}