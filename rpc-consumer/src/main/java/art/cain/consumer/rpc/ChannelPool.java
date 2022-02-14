package art.cain.consumer.rpc;

import art.cain.consumer.rpc.handler.CustomBuf2ResDecoder;
import art.cain.consumer.rpc.handler.ResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * todo zk 服务器地址需要从zk中取，spring扫描到@RpcClient修饰的类，将其保存；
 * todo zk 同时去zk中获取这些类的ip:port信息，缓存到本机；
 * todo zk 创建channel时，需要传入需要访问的类名，根据类名返回channel；
 * todo zk 缓存的刷新需要一个新的线程来完成，这个线程监听zk上节点的变化，有变化时，会发送事件通知及时刷新缓存。
 */
public class ChannelPool {

    private Channel[] channels;

    private Object[] locks;

    private static final int MAX_CHANNEL_COUNT = 4;

    private ChannelPool() {
        this.channels = new Channel[MAX_CHANNEL_COUNT];
        this.locks = new Object[MAX_CHANNEL_COUNT];
        for (int i = 0; i < MAX_CHANNEL_COUNT; i++) {
            this.locks[i] = new Object();
        }
    }

    private final static ChannelPool channelPool = new ChannelPool();

    public static ChannelPool getInstance() {
        return channelPool;
    }

    public Channel getChannel(){
        int index = new Random().nextInt(MAX_CHANNEL_COUNT);
        Channel channel = channels[index];
        if (channel != null && channel.isActive()) {
            return channel;
        }
        synchronized (locks[index]) {
            channel = channels[index];
            if (channel != null && channel.isActive()) {
                return channel;
            }
            channel = connectToServer();
            channels[index] = channel;
        }
        return channel;
    }

    private Channel connectToServer() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(5);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CustomBuf2ResDecoder());
                        pipeline.addLast(new ResponseHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("192.168.22.164", 9090));
        Channel channel = null;
        try {
            channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
