package wyh.rpc.remote.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wyh.rpc.remote.protocol.Constants;
import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcRequest;
import wyh.rpc.remote.protocol.RpcResponse;
import wyh.rpc.remote.server.LocalServiceProvider;
import wyh.rpc.utils.SingletonFactory;

public class NettyServerHandler extends ChannelInboundHandlerAdapter{
    private LocalServiceProvider localServiceProvider;
    public NettyServerHandler(){
        localServiceProvider = SingletonFactory.getSingleton(LocalServiceProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcMessage rpcMessage = (RpcMessage) msg;
        RpcRequest rpcRequest = (RpcRequest) rpcMessage.getData();

        Integer messageId = rpcMessage.getMessageId();

        Object service = localServiceProvider.getLocalService(rpcRequest.getServiceName());

        Object result = service.getClass().getMethod(rpcRequest.getMethodName() ,rpcRequest.getParamsType() ).invoke(service,rpcRequest.getParams());

        //然后构建返回消息用channel写入返回message

        RpcResponse<Object> rpcResponse = RpcResponse.builder()
                .code(200)
                .message("请求成功")
                .data(result)
                .build();

        rpcMessage.setMessageType(Constants.RESPONSE_TYPE);

        rpcMessage.setData(rpcResponse);
        ctx.writeAndFlush(rpcMessage);


    }
}
