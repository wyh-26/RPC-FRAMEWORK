package wyh.rpc.remote.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelProvider {
    private Map<String , Channel> channelCache;
    public ChannelProvider(){
        channelCache = new ConcurrentHashMap<>();
    }

    public void set(InetSocketAddress inetSocketAddress , Channel channel){
        channelCache.putIfAbsent(inetSocketAddress.toString() , channel);
    }

    public Channel get(InetSocketAddress inetSocketAddress){
        return channelCache.get(inetSocketAddress.toString());
    }

    public void delete(InetSocketAddress inetSocketAddress){
        channelCache.remove(inetSocketAddress.toString());
    }
}
