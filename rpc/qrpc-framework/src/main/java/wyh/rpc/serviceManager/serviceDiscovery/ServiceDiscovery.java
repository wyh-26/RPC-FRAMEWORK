package wyh.rpc.serviceManager.serviceDiscovery;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    public InetSocketAddress discoveryByServiceName(String serviceName);

}
