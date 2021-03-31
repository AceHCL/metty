package io.metty.eventloop;

import io.metty.NioEventLoop;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-18 10:34 AM
 */
public class NioBossEventLoopGroup extends AbstractNioEventLoopGroup{


    private static final int DEFAULT_BOSS_THREADS = Runtime.getRuntime().availableProcessors()*2;

    private final AtomicInteger bossIndex = new AtomicInteger();


    public NioBossEventLoopGroup() {
        this(DEFAULT_BOSS_THREADS);
    }

    public NioBossEventLoopGroup(int nThreads){
        super(nThreads);
    }

    @Override
    public NioEventLoop next() {
        return this.getNioEventLoops()[bossIndex.getAndIncrement()%nThreads];
    }

    @Override
    protected void init(int nThreads) {
        NioEventLoop[] nioEventLoops = this.getNioEventLoops();
        for (int i = 0; i < nThreads ; i++) {
            nioEventLoops[i] = new NioBossEventLoop(this,null,"serverthread"+i);
        }
    }
}