package wyh.rpc.remote.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalServiceProvider {
    private Map<String , Object> localServiceCache;
    public LocalServiceProvider(){
        localServiceCache = new ConcurrentHashMap<>();
    }

    public Object getLocalService(String serviceName){
        Object service = localServiceCache.get(serviceName);
        return service;
    }

    public void setLocalService(String serviceName , Object service){
        localServiceCache.putIfAbsent(serviceName , service);
    }

}
