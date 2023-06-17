package wyh.rpc.annotationProcessor;

import org.springframework.stereotype.Component;
import wyh.rpc.annotations.RpcReference;
import wyh.rpc.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.proxy.ProxyProvider;
import wyh.rpc.remote.client.Client;
import wyh.rpc.remote.server.LocalServiceProvider;
import wyh.rpc.serviceManager.serviceRegistry.ServiceRegistry;
import wyh.rpc.serviceManager.serviceRegistry.impl.ZkServiceRegistryImpl;
import wyh.rpc.utils.SingletonFactory;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    private ProxyProvider proxyProvider = SingletonFactory.getSingleton(ProxyProvider.class);
    private Client client = ExtensionLoader.getExtension(Client.class , "netty");
    private ServiceRegistry serviceRegistry = ExtensionLoader.getExtension(ServiceRegistry.class , "zk");
    private LocalServiceProvider localServiceProvider = SingletonFactory.getSingleton(LocalServiceProvider.class);
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields =  clazz.getDeclaredFields();
        //遍历bean字段
//        for(Field field: fields){
//            //如果字段被注解RpcReference注解则对该字段的值用代理对象替换
//            if(field.isAnnotationPresent(RpcReference.class)){
//                try{
//                    Object proxy = proxyProvider.getRpcClientProxy(field.getType() , client);
//                    field.setAccessible(true);
//
//                    field.set(bean , proxy);
//
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }

        if(clazz.isAnnotationPresent(RpcService.class)){

            //进行服务注册
            RpcService rpcService = clazz.getAnnotation(RpcService.class);
            String serviceName = clazz.getInterfaces()[0].getCanonicalName() + rpcService.group() + rpcService.version();
            String localhost = null;
            try {
                localhost = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            serviceRegistry.registry(serviceName , new InetSocketAddress(localhost , 5678));

            try {
                localServiceProvider.setLocalService(serviceName , clazz.newInstance());
            } catch (Exception e) {
                log.error("本地注册服务失败");
                e.printStackTrace();
            }

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields =  clazz.getDeclaredFields();
        //遍历bean字段
        for(Field field: fields){
            //如果字段被注解RpcReference注解则对该字段的值用代理对象替换
            if(field.isAnnotationPresent(RpcReference.class)){
                try{
                    Object proxy = proxyProvider.getRpcClientProxy(field.getType() , client ,field.getAnnotation(RpcReference.class));
                    field.setAccessible(true);

                    field.set(bean , proxy);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
