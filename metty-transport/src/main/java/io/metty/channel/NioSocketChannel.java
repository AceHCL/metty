package io.metty.channel;


import io.metty.util.Utils;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 8:37 AM
 */
public class NioSocketChannel extends AbstractNioChannel {

    private final int DEFAULT_LENGTH = 4;
    private ByteBuffer received;
    private boolean first;
    private byte[] pong = "pong&".getBytes();
    public AtomicBoolean pongflag = new AtomicBoolean(false);
    public AtomicBoolean pingflag = new AtomicBoolean(false);

    private final Logger logger = LoggerFactory.getLogger(NioSocketChannel.class);

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
            logger.warn("use default localAddress");
            localAddress = localAddress();
        }
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        socketChannel.socket().bind(localAddress);
    }

    @Override
    protected void doConnect(SocketAddress remoteAddress) throws Exception {
        if (remoteAddress == null){
            logger.warn("use default remoteAddress");
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
    protected void doBeginRead(){
        //开始读消息
        SocketChannel socketChannel = (SocketChannel) javaChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteBuffer.flip();
        List<Object> out = new ArrayList<>();
        readBeforeRemoveHeader(byteBuffer, out);
        if (out.size() > 0){
            for (Object e:out
            ) {
                pipeline().fireChannelRead(e);
            }
        }
    }

    private void readBeforeRemoveHeader(ByteBuffer byteBuffer, List<Object> out) {
        first = received == null;
        if (first){
            received = byteBuffer;
        }else{
            received = addBuffer(received,byteBuffer);
        }
        Object obj = getObject(received);
        while (obj != null){
            if (!doPingPong(obj)){
                out.add(obj);
            }
            obj = getObject(received);
        }
        if (received != null && !received.hasRemaining()){
            received = null;
        }
    }

    private boolean doPingPong(Object obj) {
        boolean result = false;
        if (obj != null) {
            ByteBuffer byteBuffer = (ByteBuffer) obj;
            byteBuffer.mark();
            byte[] ping = new byte[byteBuffer.limit()];
            byteBuffer.get(ping);
            String body = new String(ping);
            switch (body){
                case "ping&":
                    logger.info("ping",this);
                    this.pingflag.set(true);
                    result = true;
                    ByteBuffer pongBuffer = ByteBuffer.wrap(pong);
                    this.unsafe().write(pongBuffer);
                    break;
                case "pong&":
                    logger.info("pong",this);
                    this.pongflag.set(true);
                    result = true;
                    break;
                default:
                    break;
            }
            byteBuffer.reset();
        }
        return result;
    }

    private Object getObject(ByteBuffer received) {
        if (received.remaining() > DEFAULT_LENGTH){
            byte[] header = new byte[DEFAULT_LENGTH];
            received.mark();
            received.get(header,0,DEFAULT_LENGTH);
            int contentLength = Utils.byteArrayToInt(header);
            if (contentLength <= received.remaining()){
                byte[] contentBytes = new byte[contentLength];
                received.get(contentBytes,0,contentLength);
                ByteBuffer result = ByteBuffer.allocate(contentLength);
                result.put(contentBytes);
                result.flip();
                return result;
            }
            received.reset();
        }
        return null;
    }

    private ByteBuffer addBuffer(ByteBuffer received, ByteBuffer byteBuffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(received.limit()+byteBuffer.limit());
        newBuffer.put(received);
        newBuffer.put(byteBuffer);
        newBuffer.flip();
        return newBuffer;
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
            ByteBuffer send =sendBeforeAddHeader(msg);
            try {
                while (send.hasRemaining()){
                    socketChannel.write(send);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ByteBuffer sendBeforeAddHeader(Object msg) {
            ByteBuffer body = (ByteBuffer) msg;
            int length = body.limit();
            byte[] header = Utils.intToByteArray(length);
            ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_LENGTH+length);
            byteBuffer.put(header);
            byteBuffer.put(body);
            byteBuffer.flip();
            return byteBuffer;
        }

        @Override
        public void flush() {
            //
        }
    }

}