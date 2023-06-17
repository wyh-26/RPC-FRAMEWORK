package wyh.rpc.remote.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wyh.rpc.remote.protocol.ResultCachePool;
import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcResponse;
import wyh.rpc.utils.SingletonFactory;

import java.util.concurrent.CompletableFuture;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ResultCachePool resultCachePool;
    public NettyClientHandler(){
        resultCachePool = SingletonFactory.getSingleton(ResultCachePool.class);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcMessage message = (RpcMessage) msg;
        RpcResponse<Object> rpcResponse =  (RpcResponse<Object>) message.getData();
        Integer messageId = message.getMessageId();
        CompletableFuture<RpcResponse<Object>> completableFuture = resultCachePool.get(messageId);
        completableFuture.complete(rpcResponse);
    }
}
