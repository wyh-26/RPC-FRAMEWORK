package wyh.rpc.annotationProcessor;

import wyh.rpc.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import wyh.rpc.annotations.RpcServicePackage;

import java.util.Map;

@Slf4j
public class CustomRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private  ResourceLoader resourceLoader = null;
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String , Object> attributes = importingClassMetadata.getAnnotationAttributes(RpcServicePackage.class.getName());
        String[] basePackages = (String[]) attributes.getOrDefault("basePackages" , null);

        if(basePackages != null){

            CustomScanner customScanner = new CustomScanner(registry);
            if(resourceLoader != null){
                customScanner.setResourceLoader(resourceLoader);
            }
            customScanner.setIncludeFilter(RpcService.class);
            int serviceNum = customScanner.scan(basePackages);
            log.info("扫描到[{}]个rpc服务" , serviceNum);

        }


    }

}
