package wyh.rpc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wyh.rpc.annotations.RpcReference;
import wyh.rpc.annotations.RpcServicePackage;
import wyh.rpc.service.Talk;

@RpcServicePackage(basePackages = "wyh.rpc")
public class NettyClientTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientTest.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
