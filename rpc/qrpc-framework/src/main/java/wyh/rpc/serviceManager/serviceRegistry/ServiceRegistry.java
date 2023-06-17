package wyh.rpc.serviceManager.serviceRegistry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    public void registry(String serviceName , InetSocketAddress inetSocketAddress);
}
