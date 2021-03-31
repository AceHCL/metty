package io.metty.eventloop;

import io.metty.NioEventLoop;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-18 10:47 AM
 */
public interface NioEventLoopGroup {

    NioEventLoop next();

}