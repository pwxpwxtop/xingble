package com.example.stye;

import com.fastservice.bean.MapperBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

public class MybatisScanner extends ClassPathBeanDefinitionScanner {

    public MybatisScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            //由于我们扫描的是接口，所以这里的beanDefinition中的beanClass是接口的class，修成此处的beanDefinition
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> mapperClass = null;
            try {
                mapperClass = Class.forName(beanClassName);
                beanDefinition.getConstructorArgumentValues()
                        .addIndexedArgumentValue(0,mapperClass);
                beanDefinition.setBeanClassName(MapperBean.class.getName());
                super.getRegistry().registerBeanDefinition(mapperClass.getSimpleName(),beanDefinition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beanDefinitionHolders;
    }

    //这里我们的这个扫描器，恰恰要扫描接口
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}
