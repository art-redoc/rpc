package art.cain.provider.rpc;

import art.cain.provider.rpc.handler.CustomBuf2ReqDecoder;
import art.cain.provider.rpc.handler.ResponseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class ProviderServer {

    @Value("${ip:192.168.22.164}")
    private String ip;

    @Autowired
    private ResponseHandler responseHandler;

    public void startServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(10);
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(group, group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("server accept client port: " + ch.remoteAddress().getPort());
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CustomBuf2ReqDecoder());
                        pipeline.addLast(responseHandler);
                    }
                })
                .bind(new InetSocketAddress(ip, 9090));
        bind.sync().channel().closeFuture().sync();
    }
}
