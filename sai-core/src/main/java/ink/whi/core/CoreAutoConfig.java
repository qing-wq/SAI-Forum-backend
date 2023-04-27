package ink.whi.core;

import com.github.benmanes.caffeine.cache.Caffeine;
import ink.whi.core.cache.RedisClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author qing
 * @date 2023/4/26
 */
@Configuration
@ComponentScan(basePackages = "ink.whi.core")
public class CoreAutoConfig {

    public CoreAutoConfig(RedisTemplate<String, String> redisTemplate) {
        RedisClient.register(redisTemplate);
    }

    /**
     * 定义caffeine缓存管理器
     *
     * @return
     */
    @Bean("caffeineCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置过期时间
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 缓存空间大小
                .initialCapacity(100)
                // 最大的缓存条数
                .maximumSize(200)
        );
        return cacheManager;
    }
}
