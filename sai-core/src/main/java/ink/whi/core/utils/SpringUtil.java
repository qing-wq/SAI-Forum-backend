package ink.whi.core.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/4/29
 */
@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {

    private volatile static ApplicationContext context;
    private volatile static Environment environment;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }

    @Override
    public void setEnvironment(@NotNull Environment environment) {
        SpringUtil.environment = environment;
    }

    /**
     * 获取bean
     * @param bean
     * @return
     * @param <T>
     */
    public static <T> T getBean(Class<T> bean) {
        return context.getBean(bean);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 获取配置文件中的配置
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置文件中的配置
     * @param key
     * @param val 默认值
     * @return
     */
    public static String getConfig(String key, String val) {
        return environment.getProperty(key, val);
    }

    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }
}
