package ink.whi.web.config;

import ink.whi.core.cache.RedisClient;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author: qing
 * @Date: 2025/5/10
 */
@Configuration
public class CommonConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        RedisClient.register(stringRedisTemplate);
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("ink.whi.*", "java.util.*"));
        return converter;
    }
}
