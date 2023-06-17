package wyh.rpc.serviceManager.serviceDiscovery.impl;

import org.apache.curator.framework.CuratorFramework;
import wyh.rpc.serviceManager.centerUtils.ZkUtil;
import wyh.rpc.serviceManager.serviceDiscovery.ServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class ZkServiceDiscovery implements ServiceDiscovery {
    private CuratorFramework zkClient;
    @Override
    public InetSocketAddress discoveryByServiceName(String serviceName) {
        if(zkClient == null){
            zkClient = ZkUtil.getZkClient("192.168.98.20:2181");
        }
        String path = ZkUtil.ZK_ROOT + "/" + serviceName;
        List<String> serviceList = ZkUtil.getChildrenNodes(zkClient , path);
        int random = new Random().nextInt(serviceList.size());
        String[] address = serviceList.get(random).split(":");
        String hostname = address[0];
        int port = Integer.parseInt(address[1]);
        return new InetSocketAddress(hostname , port);
    }
}
