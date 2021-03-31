package io.metty.pipeline;

import io.metty.ChannelContext;
import io.metty.ChannelFuture;
import io.metty.ChannelHandler;
import io.metty.ChannelPipeline;
import io.metty.channel.Channel;
import io.metty.handler.ChannelInBoundHandler;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 11:49 AM
 */
public class DefaultChannelPipeline implements ChannelPipeline {

    private final static Logger logger = LoggerFactory.getLogger(DefaultChannelPipeline.class);
    private Channel channel;

    private DefaultChannelContext head;
    private DefaultChannelContext tail;
    public DefaultChannelPipeline(Channel channel) {
        this.channel = channel;
        head = new DefaultChannelContext(this,new HeadHandler());
        tail = new DefaultChannelContext(this,new TailHandler());
        head.next = tail;
        tail.prex = head;

    }

    @Override
    public void addLast(ChannelHandler channelHandler) {
        if (!isValidHandler(channelHandler)){
            return;
        }

        DefaultChannelContext newContext = new DefaultChannelContext(this,channelHandler);
        DefaultChannelContext prexContext = tail.prex;
        tail.prex = newContext;
        newContext.next = tail;
        prexContext.next = newContext;
        newContext.prex = prexContext;
        callHandlerAdded0(newContext);
    }

    private void callHandlerAdded0(DefaultChannelContext newContext) {
        try {
            newContext.callHandlerAdded();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callHandlerRemoved0(DefaultChannelContext newContext){
        try {
            newContext.callHandlerRemoved();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addLasts(List<ChannelHandler> handlers) {
        if (handlers == null || handlers.size() == 0){
            return;
        }

        for (ChannelHandler handler: handlers
             ) {
            addLast(handler);
        }
    }

    private boolean isValidHandler(ChannelHandler handler){
        if (!(handler instanceof ChannelHandler) || handler == null){
            return false;
        }
        return true;
    }


    @Override
    public void addFirst(ChannelHandler channelHandler) {
        DefaultChannelContext newContext = new DefaultChannelContext(this,channelHandler);
        DefaultChannelContext nextContext = head.next;
        head.next = newContext;
        newContext.prex = head;
        newContext.next = nextContext;
        nextContext.prex = newContext;
        callHandlerAdded0(newContext);
    }

    @Override
    public final Channel channel(){
        return channel;
    }

    @Override
    public ChannelPipeline firechannelActive() {
        tail.invokeChannelActive();
        return this;
    }

    @Override
    public final ChannelPipeline fireChannelRead(Object msg) {
        head.invokeChannelRead(msg);
        return this;
    }

    @Override
    public final ChannelPipeline fireChannelReadComplete() {
        head.invokeChannelReadComplete();
        return this;
    }

    @Override
    public final ChannelPipeline flush() {
        tail.flush();
        return this;
    }

    @Override
    public ChannelPipeline fireExceptionCaught(Throwable cause) {
        head.invokeExceptionCaught(cause);
        return this;
    }


    @Override
    public ChannelPipeline read() {
        tail.read();
        return this;
    }

    @Override
    public ChannelFuture write(Object msg) {
        return tail.write(msg);
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return tail.writeAndFlush(msg);
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        return tail.bind(localAddress);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return tail.connect(remoteAddress);
    }

    @Override
    public ChannelFuture disconnect() {
        return channel.disconnect();
    }

    @Override
    public ChannelFuture close() {
        return channel.close();
    }

    //对接socketChannel的读写方法，读写数据
    private static final class HeadHandler implements ChannelHandler {

        @Override
        public void bind(ChannelContext ctx, SocketAddress localAddress) throws Exception {
            ctx.channel().unsafe().bind(localAddress);
        }

        @Override
        public void connect(ChannelContext ctx, SocketAddress remoteAddress) throws Exception {
            ctx.channel().unsafe().connect(remoteAddress);
        }

        @Override
        public void read(ChannelContext ctx) throws Exception {
            ctx.channel().unsafe().beginRead();
        }

        @Override
        public void write(ChannelContext ctx, Object msg) throws Exception {
            ctx.channel().unsafe().write(msg);
        }

        @Override
        public void flush(ChannelContext ctx) throws Exception {
            ctx.channel().unsafe().flush();
        }

        @Override
        public void channelActive(ChannelContext ctx) throws Exception {
            ((DefaultChannelPipeline) ctx.pipeline()).onUnhandledInboundChannelActive(ctx);
        }
    }

    private static final class TailHandler implements ChannelInBoundHandler {

        @Override
        public void handlerAdded(ChannelContext ctx) throws Exception { }

        @Override
        public void handlerRemoved(ChannelContext ctx) throws Exception { }

        @Override
        public void exceptionCaught(ChannelContext ctx, Throwable cause) throws Exception {
            ((DefaultChannelPipeline) ctx.pipeline()).onUnhandledInboundException(cause);
        }

        @Override
        public void channelRead(ChannelContext ctx, Object msg) throws Exception {
            ((DefaultChannelPipeline) ctx.pipeline()).onUnhandledInboundMessage(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelContext ctx) throws Exception {
            ((DefaultChannelPipeline) ctx.pipeline()).onUnhandledInboundChannelReadComplete();
        }
    }

    private void onUnhandledInboundChannelReadComplete() {
    }

    private void onUnhandledInboundMessage(ChannelContext ctx, Object msg) {
        ByteBuffer received = (ByteBuffer) msg;
        received.flip();
        byte[] bytes = new byte[received.remaining()];
        received.get(bytes);
        String body = new String(bytes);
        if (body.equals("client")){
            String s = "received";
            logger.info(s);
            byte[] bytes1 = s.getBytes();
            received = ByteBuffer.allocate(bytes1.length);
            received.put(bytes1);
            received.flip();
            ctx.write(received);

        }
        if (body.equals("received")){
            logger.info("connected");
        }
    }

    private void onUnhandledInboundException(Throwable cause) {
        
    }

    private void onUnhandledInboundChannelActive(ChannelContext ctx){
        String send = "client";
        byte[] bytes = send.getBytes();
        ByteBuffer sendBuffer = ByteBuffer.allocate(bytes.length);
        sendBuffer.put(bytes);
        sendBuffer.flip();
        ctx.channel().write(sendBuffer);
        ctx.channel().register(SelectionKey.OP_READ);
        logger.info("clent is connecting");
    }

}