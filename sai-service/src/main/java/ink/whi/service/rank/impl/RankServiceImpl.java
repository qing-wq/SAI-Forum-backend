package ink.whi.service.rank.impl;

import com.google.common.collect.Sets;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.core.cache.RedisClient;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.rank.RankService;
import ink.whi.service.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ink.whi.api.model.constants.RedisConstants.*;
import static ink.whi.api.model.constants.RankConstants.*;

/**
 * 排行榜服务实现
 * 使用Redis Sorted Set实现用户活跃度和文章热度排行
 *
 * @author: qing
 * @Date: 2025/5/22
 */
@Slf4j
@Service
public class RankServiceImpl implements RankService {

    /**
     * 排行榜的默认返回数量
     */
    private static final int RANK_SIZE = 10;

    /**
     * 排行榜的过期时间（秒），默认为31天
     */
    private static final long RANK_EXPIRE_TIME = 31 * 24 * 60 * 60;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleDao articleDao;

    @PostConstruct
    public void init() {
        // 初始化排行榜
        refreshRankData();
    }

    /**
     * 增加用户活跃度分数
     *
     * @param userId 用户ID
     * @param score  增加的分数
     */
    public void incrementUserScore(Long userId, double score) {
        if (userId == null || score <= 0) {
            return;
        }

        try {
            String key = USER_RANK_KEY;
            redisTemplate.opsForZSet().incrementScore(key, String.valueOf(userId), score);
            redisTemplate.execute((RedisCallback<Void>) connection -> {
                connection.zIncrBy(key.getBytes(StandardCharsets.UTF_8), score, String.valueOf(userId).getBytes(StandardCharsets.UTF_8));
                return null;
            });
            log.info("Incremented user score: userId={}, score={}", userId, score);
        } catch (Exception e) {
            log.error("Failed to increment user score: userId={}, score={}", userId, score, e);
        }
    }

    /**
     * 获取用户活跃度排行榜
     *
     * @return 用户排行榜列表
     */
    @Override
    public List<SimpleUserInfoDTO> getUserRank() {
        int defaultLimit = 5;
        return getUserRank(RANK_SIZE);
    }

    /**
     * 获取指定数量的用户活跃度排行榜
     *
     * @param limit 返回的排行榜数量
     * @return 用户排行榜列表
     */
    public List<SimpleUserInfoDTO> getUserRank(int limit) {
        try {
            Set<byte[]> userIds = redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
                return connection.zRevRange(USER_RANK_KEY.getBytes(StandardCharsets.UTF_8), 0, limit - 1);
            });

            if (CollectionUtils.isEmpty(userIds)) {
                log.info("rank缓存失效！重置缓存");
                refreshRankData();
                return new ArrayList<>();
            }

            List<SimpleUserInfoDTO> result = new ArrayList<>(userIds.size());
            for (byte[] idBytes : userIds) {
                Long userId = Long.parseLong(new String(idBytes, StandardCharsets.UTF_8));
                SimpleUserInfoDTO userInfo = userService.querySimpleUserInfo(userId);
                if (userInfo != null) {
                    result.add(userInfo);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to get user rank with limit: {}", limit, e);
            return new ArrayList<>();
        }
    }

    /**
     * 刷新排行榜数据，一个月执行一次
     * 从数据库加载用户和文章数据，计算初始分数并存储到Redis
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void refreshRankData() {
        try {
            log.info("Initializing rank data...");

            // 清除现有的排行榜数据
            RedisClient.del(USER_RANK_KEY);

            // 计算初始分数，根据用户文章数量计算
            Map<Long, Long> userMap = articleDao.countArticleByUserId();
            System.out.println(userMap);
            Set<ZSetOperations.TypedTuple<String>> set = Sets.newHashSet();

            userMap.forEach((userId, articleCount) -> {
                double score = articleCount * INIT_ARTICLE_SCORE;

                // 筛除分数低的
                if (score <= 0) {
                    return;
                }
                set.add(new DefaultTypedTuple<>(userId.toString(), score));
            });

            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
            zSetOps.add(USER_RANK_KEY, set);

            // 设置过期时间
            redisTemplate.expire(USER_RANK_KEY, RANK_EXPIRE_TIME, TimeUnit.SECONDS);

            log.info("Rank data initialization completed. Rank data: {}", set);
        } catch (Exception e) {
            log.error("Failed to initialize rank data", e);
        }
    }
}
