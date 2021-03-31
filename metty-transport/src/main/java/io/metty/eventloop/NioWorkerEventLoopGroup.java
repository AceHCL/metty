package io.metty.eventloop;

import io.metty.NioEventLoop;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-18 10:36 AM
 */
public class NioWorkerEventLoopGroup extends AbstractNioEventLoopGroup {

    private static final int DEFAULT_WORKER_THREADS = Runtime.getRuntime().availableProcessors()*2;

    private final AtomicInteger workerIndex = new AtomicInteger();

    public NioWorkerEventLoopGroup(){
        this(DEFAULT_WORKER_THREADS);
    }

    public NioWorkerEventLoopGroup(int nThreads) {
        super(nThreads);
    }

    @Override
    public NioEventLoop next() {

        return this.getNioEventLoops()[workerIndex.getAndIncrement()%nThreads];
    }
    @Override
    protected void init(int nThreads) {
        NioEventLoop[] nioEventLoops = this.getNioEventLoops();
        for (int i = 0; i < nThreads ; i++) {
            //每一个loop分配一个单线程的executor；
            nioEventLoops[i] = new NioWorkerEventLoop(this,null,"workerthread"+i);
        }
    }

}