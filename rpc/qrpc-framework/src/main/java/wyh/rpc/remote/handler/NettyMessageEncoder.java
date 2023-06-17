package wyh.rpc.remote.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import wyh.rpc.compress.Compress;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.remote.protocol.Constants;
import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcRequest;
import wyh.rpc.serialize.Serialize;

public class NettyMessageEncoder extends MessageToByteEncoder<RpcMessage> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        byteBuf.writerIndex(byteBuf.writerIndex() + 4);
        byteBuf.writeInt(rpcMessage.getMessageId());
        byteBuf.writeByte(rpcMessage.getMessageType());
        byteBuf.writeByte(rpcMessage.getCompressType());
        byteBuf.writeByte(rpcMessage.getSerializeType());
        Serialize serializer = ExtensionLoader.getExtension(Serialize.class , "protostuff");
        Object data = rpcMessage.getData();
        byte[] dataBytes = null;
        dataBytes = serializer.serialize(data);
        Compress compress = ExtensionLoader.getExtension(Compress.class , "gzip");
        dataBytes = compress.compress(dataBytes);

        byteBuf.writeBytes(dataBytes);
        int dataLength = dataBytes.length;
        int totalLength = dataLength + Constants.HEAD_LENGTH;
        int writerIndex = byteBuf.writerIndex();
        byteBuf.writerIndex(0);
        byteBuf.writeInt(totalLength);
        byteBuf.writerIndex(writerIndex);
    }
}
