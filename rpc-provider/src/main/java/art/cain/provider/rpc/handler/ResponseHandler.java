package art.cain.provider.rpc.handler;

import art.cain.provider.rpc.RpcServerService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ResponseHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private RpcServerService rpcServerService;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf response = rpcServerService.handle(msg);
        ctx.writeAndFlush(response);
    }
}
