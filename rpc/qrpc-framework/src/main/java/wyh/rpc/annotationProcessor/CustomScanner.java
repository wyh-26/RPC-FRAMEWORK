package wyh.rpc.annotationProcessor;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

public class CustomScanner extends ClassPathBeanDefinitionScanner {
    public CustomScanner(BeanDefinitionRegistry registry){
        super(registry);
    }

    public void setIncludeFilter(Class<? extends Annotation> annoType){
        addIncludeFilter(new AnnotationTypeFilter(annoType));
    }

    public void setExcludeFilter(Class<? extends  Annotation> annoType){
        addExcludeFilter(new AnnotationTypeFilter(annoType));
    }
}
