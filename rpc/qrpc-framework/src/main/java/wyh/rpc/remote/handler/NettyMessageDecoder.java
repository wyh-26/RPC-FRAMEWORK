package wyh.rpc.remote.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import wyh.rpc.compress.Compress;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.remote.protocol.Constants;
import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcRequest;
import wyh.rpc.remote.protocol.RpcResponse;
import wyh.rpc.serialize.Serialize;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(){

        super(Constants.MAX_FRAME_LENGTH , 0 , 4 ,-4 ,0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf decoded = (ByteBuf) super.decode(ctx, in);
        Serialize serialize = ExtensionLoader.getExtension(Serialize.class , "protostuff");

        RpcMessage rpcMessage = new RpcMessage();
        System.out.println(decoded);
        int totalLength = decoded.readInt();

        //System.out.println("解码00000000000000000000000000000000000000000000000000000000");
        int dataLength = totalLength - Constants.HEAD_LENGTH;

        rpcMessage.setMessageId(decoded.readInt());
        rpcMessage.setMessageType(decoded.readByte());
        rpcMessage.setCompressType(decoded.readByte());
        rpcMessage.setSerializeType(decoded.readByte());

        byte[] data = new byte[dataLength];
        //下面可能是错的
        decoded.readBytes(data);

        Compress compress = ExtensionLoader.getExtension(Compress.class , "gzip");
        data = compress.uncompress(data);
        if(rpcMessage.getMessageType() == Constants.REQUEST_TYPE){
            rpcMessage.setData(serialize.unSerialize( data , RpcRequest.class));
        }else if(rpcMessage.getMessageType() == Constants.RESPONSE_TYPE){
            rpcMessage.setData(serialize.unSerialize(data , RpcResponse.class));
        }
        return rpcMessage;

    }
}
