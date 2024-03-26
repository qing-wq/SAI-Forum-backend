package ink.whi.web.ai;

import dev.langchain4j.service.spring.AiService;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

//        String[] applicationBean = beanFactory.getBeanNamesForAnnotation(SpringBootApplication.class);
//        BeanDefinition applicationBeanDefinition = beanFactory.getBeanDefinition(applicationBean[0]);
//        String basePackage = applicationBeanDefinition.getResolvableType().resolve().getPackage().getName();

        return basePackages;
    }
}
