package wyh.rpc.remote.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import wyh.rpc.enums.CompressTypeEnum;
import wyh.rpc.enums.SerializeTypeEnum;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.remote.client.ChannelProvider;
import wyh.rpc.remote.client.Client;
import wyh.rpc.remote.handler.NettyClientHandler;
import wyh.rpc.remote.handler.NettyMessageDecoder;
import wyh.rpc.remote.handler.NettyMessageEncoder;
import wyh.rpc.remote.protocol.*;
import wyh.rpc.serviceManager.serviceDiscovery.ServiceDiscovery;
import wyh.rpc.utils.AtomicNumberUtil;
import wyh.rpc.utils.SingletonFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyClientImpl implements Client {
    private EventLoopGroup eventLoopGroup;
    private Bootstrap clientBootstrap;
    private ChannelProvider channelProvider;
    private ServiceDiscovery serviceDiscovery;
    private ResultCachePool resultCachePool;

    public NettyClientImpl(){
        log.info("准备启动netty客户端");
        eventLoopGroup = new NioEventLoopGroup();
        clientBootstrap = new Bootstrap();

        clientBootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new NettyMessageEncoder())
                                .addLast(new NettyMessageDecoder())
                                .addLast(new NettyClientHandler());
                    }
                });
        channelProvider = SingletonFactory.getSingleton(ChannelProvider.class);
        serviceDiscovery = ExtensionLoader.getExtension(ServiceDiscovery.class , "zk");
        resultCachePool = SingletonFactory.getSingleton(ResultCachePool.class);
        log.info("客户端启动成功");


    }


    @Override
    public CompletableFuture<RpcResponse<Object>> sendRpcMessage(RpcRequest rpcRequest) {
        //根据请求消息发现服务，然后获取channel发送消息,然后向ResultCachePool存入对应请求的CompleteableFuture

        InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryByServiceName(rpcRequest.getServiceName());
        log.info("发现了服务{}对应的服务地址{}" , rpcRequest.getServiceName() , inetSocketAddress.toString());
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageId(AtomicNumberUtil.getAtomicInteger())
                .messageType(Constants.REQUEST_TYPE)
                .compressType(CompressTypeEnum.GZIP.getCode())
                .serializeType(SerializeTypeEnum.PROTOSTUFF.getCode())
                .data(rpcRequest)
                .build();
        System.out.println("开始发送消息");
        Channel channel = getChannel(inetSocketAddress);
        channel.writeAndFlush(rpcMessage);
        CompletableFuture<RpcResponse<Object>> completableFuture = new CompletableFuture<>();
        resultCachePool.put(rpcMessage.getMessageId() , completableFuture);
        return completableFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress){
        Channel channel = channelProvider.get(inetSocketAddress);
        if(channel == null){
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress , channel);
        }
        return channel;
    }

    public Channel doConnect(InetSocketAddress inetSocketAddress){
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        clientBootstrap.connect(inetSocketAddress.getHostName() , inetSocketAddress.getPort()).addListener((ChannelFutureListener) future -> {
             if(future.isSuccess()){
                 completableFuture.complete(future.channel());
             }else{
                 log.error("客户端连接{}失败" , inetSocketAddress.getHostName());
             }
        });
        Channel channel = null;
        try {
            channel = completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
}
