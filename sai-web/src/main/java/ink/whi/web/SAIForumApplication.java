package ink.whi.web;

import ink.whi.web.hook.interceptor.GlobalInterceptor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@EnableCaching
@EnableRabbit
@ServletComponentScan
@EnableAspectJAutoProxy
@SpringBootApplication
public class SAIForumApplication implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/error");
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SAIForumApplication.class).allowCircularReferences(true).run(args);
    }
}
