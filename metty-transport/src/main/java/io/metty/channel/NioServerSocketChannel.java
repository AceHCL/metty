package io.metty.channel;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.*;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 8:36 AM
 */
public class NioServerSocketChannel extends AbstractNioChannel  {

    private static ServerSocketChannel newServerSocket(){
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocketChannel;
    }

    public NioServerSocketChannel() {
        super(newServerSocket());
    }

    public NioSocketChannel accept() throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) javaChannel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        return new NioSocketChannel(socketChannel);
    }

    public ServerSocketChannel getServerSockeetChannel(){
        return (ServerSocketChannel) javaChannel();
    }

    @Override
    protected void doFinishConnect() throws Exception {

    }

    @Override
    protected void doBind(SocketAddress localAddress) throws Exception {
        if (localAddress == null){
            localAddress = localAddress();
        }
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) javaChannel();
        serverSocketChannel.socket().bind(localAddress,1024);
    }

    @Override
    protected void doConnect(SocketAddress remoteAddress) throws Exception {
        if (remoteAddress == null){
            remoteAddress = remoteAddress();
        }
        //
    }

    @Override
    protected void doBeginRead() throws Exception {

    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new NioMessageUnsafe();
    }

    private final class NioMessageUnsafe extends AbstractNioUnsafe{

        @Override
        public void read() {

        }

        @Override
        public void forceFlush() {

        }

        @Override
        public void write(Object msg) {

        }

        @Override
        public void flush() {

        }
    }


}