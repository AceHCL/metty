package example3.server;

import example3.People;
import io.metty.ChannelContext;
import io.metty.handler.ChannelInBoundHandlerAdapter;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 4:13 下午
 */
public class InHandler extends ChannelInBoundHandlerAdapter {

    @Override
    public void channelRead(ChannelContext ctx, Object msg) throws Exception {
        People people = (People) msg;
        System.out.println(people.getName());
        System.out.print(people.getAge());
    }
}