package wyh.rpc.serviceManager.centerUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkUtil {
    private static CuratorFramework zkClient;
    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000 , 3);
    private static Map<String , List<String>> serviceNameToAddress = new ConcurrentHashMap<>();
    private static Set<String> localNodes = ConcurrentHashMap.newKeySet();
    public static String ZK_ROOT = "/qrpc";

    public static CuratorFramework getZkClient(String address){
        if(zkClient == null || zkClient.getState() != CuratorFrameworkState.STARTED){
            zkClient = CuratorFrameworkFactory.builder()
                    .connectString(address)
                    .sessionTimeoutMs(5000)
                    .connectionTimeoutMs(5000)
                    .retryPolicy(retryPolicy)
                    .build();
            zkClient.start();
        }
        return zkClient;
    }

    public static void createPersistentNode(CuratorFramework client , String path){
        try {
            if(localNodes.contains(path) || client.checkExists().forPath(path) != null){
                log.info("节点{}已存在" , path);
            }else{
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);

            }
            localNodes.add(path);

        } catch (Exception e) {
            log.error("向zookeeper创建节点{}失败" , path);
        }

    }

    public static List<String> getChildrenNodes(CuratorFramework client ,  String parentPath){
        List<String> childrenNodes = null;
        childrenNodes = serviceNameToAddress.get(parentPath);
        if(childrenNodes == null){
            try {
                childrenNodes = client.getChildren().forPath(parentPath);
                serviceNameToAddress.putIfAbsent(parentPath , childrenNodes);
            } catch (Exception e) {
                log.error("获取{}的子节点路劲失败" , parentPath);
            }
        }

        return childrenNodes;
    }

    public static void removeNodes(CuratorFramework client , InetSocketAddress inetSocketAddress) {
        for(String node:localNodes){
            if(node.endsWith(inetSocketAddress.toString())){
                try {
                    client.delete().forPath(node);
                } catch (Exception e) {
                    log.error("删除节点{}失败" , node);
                }
            }
        }
    }

    //由于是分布式的，还因该能够在其他服务提供者更新了zookeeper后本地缓存能够做出对应更新
}
