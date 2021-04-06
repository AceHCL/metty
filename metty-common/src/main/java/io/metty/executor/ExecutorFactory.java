package io.metty.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-24 8:27 PM
 */
public class ExecutorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorFactory.class);

    public static final Executor newExecutor(){
        logger.info("create a new executor");
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(50000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1, 10,TimeUnit.SECONDS,arrayBlockingQueue, new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }
}