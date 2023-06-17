package wyh.rpc.proxy;

import lombok.extern.slf4j.Slf4j;
import wyh.rpc.annotations.RpcReference;
import wyh.rpc.enums.CompressTypeEnum;
import wyh.rpc.enums.SerializeTypeEnum;
import wyh.rpc.loader.ExtensionLoader;
import wyh.rpc.remote.client.Client;
import wyh.rpc.remote.protocol.Constants;
import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcRequest;
import wyh.rpc.remote.protocol.RpcResponse;
import wyh.rpc.utils.AtomicNumberUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
@Slf4j
public class ProxyProvider {
    @SuppressWarnings("unchecked")
    public <T> T getRpcClientProxy(Class<T> clazz  , Client client , RpcReference rpcReference){

        InvocationHandler rpcHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(clazz.getCanonicalName());
                System.out.println(rpcReference.group());

                RpcRequest rpcRequest = RpcRequest.builder()
                        .interfaceName(clazz.getCanonicalName())
                        .group(rpcReference.group())
                        .version(rpcReference.version())
                        .methodName(method.getName())
                        .paramsType(method.getParameterTypes())
                        .params(args)
                        .build();
                System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                CompletableFuture<RpcResponse<Object>> completableFuture =  client.sendRpcMessage(rpcRequest);

                RpcResponse<Object> rpcResponse = completableFuture.get();
                return rpcResponse.getData();



            }
        };

        return (T) Proxy.newProxyInstance(ProxyProvider.class.getClassLoader() , new Class<?>[]{clazz}, rpcHandler);


    }


}
