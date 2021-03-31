package io.metty.eventloop;

import io.bootstrap.Bootstrap;
import io.metty.NioEventLoop;

/**
 * 描述:
 *
 * @author ace-huangc
 * @create 2021-03-18 10:48 AM
 */
public abstract class AbstractNioEventLoopGroup implements NioEventLoopGroup {

    private final NioEventLoop[] nioEventLoops;

    private  Bootstrap bootstrap;

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    protected final int nThreads;


    protected AbstractNioEventLoopGroup(int nThreads) {
        if (nThreads <= 0){
            throw new IllegalArgumentException(" illeagal Boss nThreads ");
        }
        this.nioEventLoops = new NioEventLoop[nThreads];
        this.nThreads = nThreads;
        init(nThreads);
    }

    public NioEventLoop[] getNioEventLoops() {
        return nioEventLoops;
    }

    @Override
    public abstract NioEventLoop next();

    protected abstract void init(int nThreads);
}