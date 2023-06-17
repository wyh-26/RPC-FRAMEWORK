package wyh.rpc.serviceManager.serviceRegistry.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import wyh.rpc.serviceManager.centerUtils.ZkUtil;
import wyh.rpc.serviceManager.serviceRegistry.ServiceRegistry;

import java.net.InetSocketAddress;

@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {

    private CuratorFramework zkClient;
    @Override
    public void registry(String serviceName, InetSocketAddress inetSocketAddress) {

        if(zkClient == null){

            zkClient = ZkUtil.getZkClient("192.168.98.20:2181");

        }
        String path = ZkUtil.ZK_ROOT + "/" + serviceName + inetSocketAddress.toString();
        ZkUtil.createPersistentNode(zkClient , path);
    }
}
