package wyh.rpc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wyh.rpc.annotations.RpcServicePackage;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.remote.server.Server;

@RpcServicePackage(basePackages = "wyh.rpc")
public class NettyServerTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerTest.class);

        Server server = ExtensionLoader.getExtension(Server.class , "netty");
        server.start();

    }
}
