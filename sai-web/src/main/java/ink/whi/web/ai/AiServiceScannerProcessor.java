package ink.whi.web.ai;

import dev.langchain4j.service.spring.AiService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: qing
 * @Date: 2024/8/18
 */
@Component
public class AiServiceScannerProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathAiServiceScanner classPathAiServiceScanner = new ClassPathAiServiceScanner(registry, false);
        classPathAiServiceScanner.addIncludeFilter(new AnnotationTypeFilter(AiService.class));
        Set<String> basePackages = getBasePackages((ConfigurableListableBeanFactory) registry);
        classPathAiServiceScanner.scan(StringUtils.toStringArray(basePackages));
    }

    private Set<String> getBasePackages(ConfigurableListableBeanFactory beanFactory) {
        Set<String> basePackages = new LinkedHashSet<>();

        List<String> autoConfigPackages = AutoConfigurationPackages.get(beanFactory);
        basePackages.addAll(autoConfigPackages);

        String[] beanNames = beanFactory.getBeanNamesForAnnotation(ComponentScan.class);
        for (String beanName : beanNames) {
            Class<?> beanClass = beanFactory.getType(beanName);
            if (beanClass != null) {
                ComponentScan componentScan = beanClass.getAnnotation(ComponentScan.class);
                if (componentScan != null) {
                    Collections.addAll(basePackages, componentScan.value());
                    Collections.addAll(basePackages, componentScan.basePackages());
                    for (Class<?> basePackageClass : componentScan.basePackageClasses()) {
                        basePackages.add(ClassUtils.getPackageName(basePackageClass));
                    }
                }
            }
        }

        String[] applicationBean = beanFactory.getBeanNamesForAnnotation(SpringBootApplication.class);
        SpringBootApplication springbootApplication = AnnotationUtils.findAnnotation(beanFactory.getType(applicationBean[0]), SpringBootApplication.class);
        if (springbootApplication != null) {
            Collections.addAll(basePackages, springbootApplication.scanBasePackages());
            for (Class<?> aClass : springbootApplication.scanBasePackageClasses()) {
                basePackages.add(ClassUtils.getPackageName(aClass));
            }
        }
        return basePackages;
    }
}
