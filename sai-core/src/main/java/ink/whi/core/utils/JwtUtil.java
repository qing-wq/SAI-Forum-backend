package ink.whi.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Date;


/**
 * JWT工具类
 * @author: qing
 * @Date: 2023/4/26
 */
@Slf4j
@Data
public class JwtUtil {
//    @Value("${jwt.key}")
//    private String JWT_KEY;
    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String Authorization = "Authorization";
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000L;

    /**
     * 生成token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(SpringUtil.getConfig("jwt.key"));
        return JWT.create()
                .withSubject(Long.toString(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ONE_MONTH))
                .sign(algorithm);
    }

    /**
     * 校验token
     *
     * @param token
     * @return userId
     */
    public static Long isVerify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SpringUtil.getConfig("jwt.key"));
        String userId = JWT.require(algorithm)
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        return Long.parseLong(userId);
    }

    /**
     * 检验token是否需要更新
     *
     * @param token
     * @return
     */
    public static boolean isNeedUpdate(String token) {
        Date expiresAt = JWT.require(Algorithm.HMAC256(SpringUtil.getConfig("jwt.key")))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getExpiresAt();
        // 小于半个月更新
        return (expiresAt.getTime() - System.currentTimeMillis()) < (ONE_MONTH >> 1);
    }

}
