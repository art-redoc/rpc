package art.cain.consumer.rpc.handler;

import art.cain.common.rpc.models.Header;
import art.cain.common.rpc.models.ResponsePackage;
import art.cain.consumer.rpc.ResponseRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponsePackage responsePackage = (ResponsePackage) msg;
        Header header = responsePackage.getHeader();
        Object response = responsePackage.getResponse();
        ResponseRegistry.putResponse(header.getRequestId(), response);
    }
}
