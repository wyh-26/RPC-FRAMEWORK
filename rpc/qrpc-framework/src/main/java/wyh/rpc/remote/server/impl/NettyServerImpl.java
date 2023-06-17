package wyh.rpc.remote.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import wyh.rpc.remote.handler.NettyMessageDecoder;
import wyh.rpc.remote.handler.NettyMessageEncoder;
import wyh.rpc.remote.handler.NettyServerHandler;
import wyh.rpc.remote.server.Server;

@Slf4j
public class NettyServerImpl implements Server {
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private Integer port;
    private ServerBootstrap serverBootstrap;
    @Override
    public void start() {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup , workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG , 1024)
                .childOption(ChannelOption.SO_KEEPALIVE , true)
                .childOption(ChannelOption.TCP_NODELAY , true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new NettyMessageEncoder())
                                .addLast(new NettyMessageDecoder())
                                .addLast(new NettyServerHandler());
                    }
                });

        try {
            log.info("服务开始启动。。。");
            ChannelFuture channelFuture = serverBootstrap.bind(5678).sync();
            log.info("服务启动成功。。。");
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
