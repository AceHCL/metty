package io.metty;

import java.nio.channels.Selector;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-18 11:07 AM
 */
public interface NioEventLoop {
    Selector getSelector();
}