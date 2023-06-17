package wyh.rpc.utils;

import java.util.concurrent.ConcurrentHashMap;

public class SingletonFactory {
    private static ConcurrentHashMap<Class<?> , Object> instanceCache = new ConcurrentHashMap<>();
    public static<T> T getSingleton(Class<T> clazz){
        Object instance = instanceCache.get(clazz);
        if(instance == null) {
            try {
                instanceCache.put(clazz , clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        instance = instanceCache.get(clazz);
        return (T) instance;
    }
}
