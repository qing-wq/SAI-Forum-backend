package dev.langchain4j.service.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author: qing
 * @Date: 2024/8/18
 */
public class ClassPathAiServiceScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathAiServiceScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
        addIncludeFilter(new AnnotationTypeFilter(AiService.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        return annotationMetadata.isInterface() && annotationMetadata.isIndependent();
    }
}
