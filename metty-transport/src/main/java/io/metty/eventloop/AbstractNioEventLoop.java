package io.metty.eventloop;

import io.metty.channel.AbstractChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-14 9:31 PM
 */
public abstract class AbstractNioEventLoop implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AbstractNioEventLoop.class);
    private final Executor executor;

    protected final AbstractNioEventLoopGroup parent;

    protected  Selector selector;

    protected final AtomicBoolean wakeUp = new AtomicBoolean();

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

    private String threadName;


    protected final ConcurrentHashMap<AbstractSelectableChannel, AbstractChannel> map = new ConcurrentHashMap<>();

    public void mapPut(AbstractSelectableChannel socketchannel, AbstractChannel channel){
        map.put(socketchannel,channel);
    }

    public AbstractNioEventLoop(AbstractNioEventLoopGroup parent, Executor executor, String threadName) {
        if (executor == null){
            executor = Executors.newSingleThreadExecutor();
        }
        this.parent = parent;
        this.executor = executor;
        this.threadName = threadName;
        openSelector();
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.execute(this);
    }

    private void processTaskQueue(){
        while (true){
            final Runnable task = taskQueue.poll();
            if (task == null){
                break;
            }
            task.run();
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        log.info("thread:   "+this.threadName+"------start");
        while (true){
            try {
                wakeUp.set(false);
                select(selector);
                processTaskQueue();
                process(selector);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected final void registerTask(Runnable task){
        taskQueue.add(task);
        Selector selector= this.selector;
        if (selector != null){
            if (wakeUp.compareAndSet(false,true)){
                selector.wakeup();
            }
        }else{
            taskQueue.remove();
        }
    }

    protected abstract int select(Selector selector) throws IOException;

    /**
     * selector的业务处理
     */
    protected abstract void process(Selector selector)throws IOException;
}