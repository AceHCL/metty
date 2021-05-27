package example3.client;

import example3.People;
import io.metty.ChannelContext;
import io.metty.handler.ChannelOutBoundHandlerAdpter;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 4:10 下午
 */
public class OutHandler extends ChannelOutBoundHandlerAdpter {

    @Override
    public void channelActive(ChannelContext ctx) throws Exception {
        People people = new People("huangchenglong",23);
        ctx.write(people);
    }
}