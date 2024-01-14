package ink.whi.core.limit;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 限流规则：计数器限流
 * limit.period() 计数器时间
 * limit.count()  最大请求次数
 */
@Aspect
@Component
public class LimitAspect {

    private final StringRedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);
    private static final String PREFIX = "LIMIT_";

    public LimitAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(ink.whi.core.limit.Limit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);

        String key = getCombinKey(limit, signatureMethod);
        List<String> keys = Collections.singletonList(key);

        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

        Long count = redisTemplate.execute(redisScript, keys, String.valueOf(limit.count()), String.valueOf(limit.period()));
        if (null != count && count.intValue() <= limit.count()) {
            logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
            return point.proceed();
        } else {
            throw BusinessException.newInstance(StatusEnum.ACCESS_FREQUENT);
        }
    }

    private String getCombinKey(Limit limit, Method signatureMethod) {
        // combinKey = 资源key + 用户key
        StringBuilder combinKey = new StringBuilder(PREFIX);

        // 资源key
        String key = limit.key();
        combinKey.append(Objects.requireNonNullElseGet(key, signatureMethod::getName)).append("_");

        // 用户key
        LimitType limitType = limit.limitType();
        switch (limitType) {
            case CUSTOMER:
                combinKey.append(ReqInfoContext.getReqInfo().getUserId());
                break;
            case IP:
                combinKey.append(ReqInfoContext.getReqInfo().getClientIp());
                break;
            default:
                break;
        }
        return combinKey.toString();
    }

    /**
     * 限流脚本
     */
    private String buildLuaScript() {
        return """
                local key = KEYS[1]
                local limitCount = tonumber(ARGV[1])
                local limitTime = tonumber(ARGV[2])
                
                local currentCount = redis.call('get',key)
                if type(currentCount) == "number" and tonumber(currentCount) > limitCount then
                    return tonumber(currentCount)
                end
                
                currentCount = redis.call("incr",key)
                if tonumber(currentCount) == 1 then
                    redis.call("expire",key,limitTime)
                end
                return tonumber(currentCount)
                """;
    }
}
