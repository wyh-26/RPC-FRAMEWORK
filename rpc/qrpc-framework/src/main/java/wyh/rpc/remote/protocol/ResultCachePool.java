package wyh.rpc.remote.protocol;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ResultCachePool {
    private Map<Integer , CompletableFuture<RpcResponse<Object>>> futureResultCache = new ConcurrentHashMap<>();
    public void put(Integer MessageId , CompletableFuture<RpcResponse<Object>> completableFuture){
        futureResultCache.putIfAbsent(MessageId , completableFuture);
    }

    public CompletableFuture<RpcResponse<Object>> get(Integer messageId ){
        CompletableFuture<RpcResponse<Object>> future = futureResultCache.get(messageId);
        if(future != null){
            futureResultCache.remove(messageId);
        }
        return future;
    }
}
