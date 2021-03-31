package io.metty.channel;


import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 8:37 AM
 */
public class NioSocketChannel extends AbstractNioChannel {

    private static SocketChannel newSocket(){
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }

    public NioSocketChannel() {
        super(newSocket());
    }

    public NioSocketChannel(SocketChannel socketChannel){
        super(socketChannel);
    }

    @Override
    protected SelectableChannel javaChannel() {
        return super.javaChannel();
    }

    public SocketChannel getSocketChannel(){
        return (SocketChannel) super.javaChannel();
    }

    @Override
    protected void doBind(SocketAddress localAddress) throws Exception {
        if (localAddress == null){
            localAddress = localAddress();
        }
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        socketChannel.socket().bind(localAddress);
    }

    @Override
    protected void doConnect(SocketAddress remoteAddress) throws Exception {
        if (remoteAddress == null){
            remoteAddress = remoteAddress();
        }
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        socketChannel.connect(remoteAddress);
        doFinishConnect();
    }



    public void register(Selector selector, int opRead) throws ClosedChannelException {
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        socketChannel.register(selector,opRead);
    }

    @Override
    protected void doBeginRead() throws Exception {
        //开始读消息
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        pipeline().fireChannelRead(byteBuffer);
    }

    @Override
    protected void doFinishConnect() throws Exception {
        //连接过后需要执行的操作

    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new NioSocketChannelUnsafe();
    }


    private final class NioSocketChannelUnsafe extends AbstractNioUnsafe {

        @Override
        public void read() {

        }

        @Override
        public void forceFlush() {

        }

        @Override
        public void write(Object msg) {
            Objects.requireNonNull(msg,"send msg null");
            SocketChannel socketChannel = (SocketChannel) javaChannel();
            ByteBuffer send = (ByteBuffer) msg;
            try {
                while (send.hasRemaining()){
                    socketChannel.write(send);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void flush() {
            //
        }
    }

}