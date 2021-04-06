package io.metty.pipeline;

import io.metty.ChannelContext;
import io.metty.ChannelFuture;
import io.metty.ChannelHandler;
import io.metty.ChannelPipeline;
import io.metty.channel.Channel;
import io.metty.handler.ChannelInBoundHandler;

import java.net.SocketAddress;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-15 11:50 AM
 */
public class DefaultChannelContext implements ChannelContext {

    private ChannelHandler channelHandler;
    private ChannelPipeline pipeline;
    private final int flag;
    private final int DEFAULT_INBOUND = 0;
    private final int DEFAULT_OUTBOUND = 1;
    private final int DEFAULT_BOUND = 2;

    protected DefaultChannelContext next;

    protected DefaultChannelContext prex;

    public DefaultChannelContext(ChannelPipeline pipeline, ChannelHandler channelHandler) {
        this.pipeline = pipeline;
        this.channelHandler = channelHandler;
        if (!(channelHandler instanceof ChannelHandler)){
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (channelHandler instanceof ChannelHandler){
            this.flag = DEFAULT_BOUND;
        }else if (channelHandler instanceof ChannelInBoundHandler){
            this.flag = DEFAULT_INBOUND;
        }else{
            this.flag = DEFAULT_OUTBOUND;
        }
    }
    @Override
    public ChannelHandler handler() {
        return channelHandler;
    }

    @Override
    public Channel channel() {
        return pipeline.channel();
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public ChannelContext firechannelRegistered() {
        findAndInvokeChannelRegistered();
        return this;
    }

    private void findAndInvokeChannelRegistered() {
        DefaultChannelContext context = findContextInbound();
        context.invokeChannelRegistered();
    }

    private void invokeChannelRegistered() {
        try {
            handler().channelRegistered(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelContext fireChannelRead(Object msg) {
        findAndInvokeChannelRead(msg);
        return this;
    }

    private void findAndInvokeChannelRead(Object msg) {
        DefaultChannelContext context = findContextInbound();
        context.invokeChannelRead(msg);
    }

    public void invokeChannelRead(Object msg) {
        try {

            handler().channelRead(this,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DefaultChannelContext findContextInbound(){
        DefaultChannelContext ctx = this;
        do {
            ctx = ctx.next;
        }while (ctx != null &&!ctx.isProcessInboundDirectly());
        return ctx;
    }

    private DefaultChannelContext findContextNext(){
        return this.next;
    }

    private boolean isProcessInboundDirectly() {
        return this.flag == DEFAULT_INBOUND || this.flag == DEFAULT_BOUND;
    }
    private boolean isProcessOutboundDirectly() {
        return this.flag == DEFAULT_OUTBOUND || this.flag == DEFAULT_BOUND;
    }

    @Override
    public ChannelContext fireChannelReadComplete() {
        findAndInvokeChannelReadComplete();
        return this;
    }

    @Override
    public ChannelContext firechannelActive() {
        findAndInvokeChannelActive();
        return this;
    }

    private void findAndInvokeChannelActive() {
        DefaultChannelContext context = findContextOutbound();
        context.invokeChannelActive();
    }

    public void invokeChannelActive() {
        try {
            handler().channelActive(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndInvokeChannelReadComplete() {
        DefaultChannelContext context = findContextInbound();
        context.invokeChannelReadComplete();
    }

    public void invokeChannelReadComplete() {
        try {
            handler().channelReadComplete(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public ChannelContext read() {
        findAndInvokeRead();
        return this;
    }

    private void findAndInvokeRead() {
        DefaultChannelContext context = findContextOutbound();
        context.invokeRead(context);
    }

    private DefaultChannelContext findContextOutbound() {
        DefaultChannelContext ctx = this;
        do {
            ctx = ctx.prex;
        }while (ctx != null && !isProcessOutboundDirectly());
        return ctx;
    }

    private void invokeRead(DefaultChannelContext context) {
        try {
            handler().read(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelFuture write(Object msg) {
        findAndInvokeWrite(msg);
        return null;
    }

    private void findAndInvokeWrite(Object msg) {
        DefaultChannelContext context = findContextOutbound();
        context.invokeWrite(context,msg);
    }

    private void invokeWrite(DefaultChannelContext context,Object msg) {

        try {
            handler().write(context,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        findAndinvokeConnect(remoteAddress);
        return null;
    }

    private void findAndinvokeConnect(SocketAddress remoteAddress) {
        DefaultChannelContext context = findContextOutbound();
        context.invokeConnect(this,remoteAddress);
    }

    private void invokeConnect(DefaultChannelContext context, SocketAddress remoteAddress) {
        try {
            handler().connect(context,remoteAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        findAndInvokeBind(localAddress);
        return null;
    }

    private void findAndInvokeBind(SocketAddress localAddress) {
        DefaultChannelContext context = findContextOutbound();
        context.invokeBind(this,localAddress);
    }

    private void invokeBind(DefaultChannelContext context, SocketAddress localAddress) {
        try {
            handler().bind(context,localAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void callHandlerAdded() throws Exception {
        handler().handlerAdded(this);
    }

    void callHandlerRemoved() throws Exception{
        handler().handlerRemoved(this);
    }

    @Override
    public ChannelContext flush() {
        return null;
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return null;
    }

    @Override
    public ChannelFuture disconnect() {
        return null;
    }

    @Override
    public ChannelFuture close() {
        return null;
    }

    @Override
    public ChannelContext fireExceptionCaught(Throwable cause) {
        return null;
    }

    public void invokeExceptionCaught(Throwable cause) {

    }


}