package wyh.rpc.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExtensionLoader {
    private static Map<String , Map<String , Object>> extensionCache = new ConcurrentHashMap<>();
    public static<T> T getExtension(Class<T> extensionInterface , String extensionImplName){
        String extensionInterfaceName = extensionInterface.getCanonicalName();
        Map<String , Object> extensionMap = extensionCache.get(extensionInterfaceName);
        Object res = null;
        if(extensionMap == null){
            extensionMap = new ConcurrentHashMap<>();
            ClassPathResource classPathResource = new ClassPathResource("EXTENSION/" + extensionInterfaceName);

            try {
                InputStream inputStream = classPathResource.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while((line = in.readLine())!= null){
                    String[] extensionNameAndImpl = line.split("=");
                    String extensionName = extensionNameAndImpl[0];
                    String implName = extensionNameAndImpl[1];
                    Object instance = classPathResource.getClassLoader().loadClass(implName).newInstance();
                    extensionMap.put(extensionName , instance);
                }
                extensionCache.put(extensionInterfaceName , extensionMap);

            } catch (Exception e) {
                log.error("读取{}文件失败",extensionInterfaceName);
            }
        }

        res = extensionMap.get(extensionImplName);
        if(res == null){
            log.error("不存在{}插件" , extensionImplName);
        }
        return (T) res;
    }
}
