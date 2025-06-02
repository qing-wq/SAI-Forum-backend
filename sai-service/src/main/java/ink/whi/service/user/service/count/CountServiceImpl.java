package ink.whi.service.user.service.count;

import cn.hutool.json.JSONUtil;
import ink.whi.api.model.constants.RedisConstants;
import ink.whi.api.model.vo.article.dto.ArticleFootCountDTO;
import ink.whi.core.cache.RedisClient;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.service.CountService;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 计数服务
 * todo: 计数相关后续使用redis来做
 *
 * @author qing
 * @date 2023/4/28
 */
@Slf4j
@Service
public class CountServiceImpl implements CountService {

    private static final HashedWheelTimer HASHED_WHEEL_TIMER = new HashedWheelTimer();

    private final UserFootDao userFootDao;

    @Autowired
    private CommentReadService commentReadService;

    public CountServiceImpl(UserFootDao userFootDao) {
        this.userFootDao = userFootDao;
    }

    @Override
    public ArticleFootCountDTO queryArticleCountInfoByUserId(Long userId) {
        return userFootDao.countArticleByUserId(userId);
    }

    @Override
    public ArticleFootCountDTO queryArticleCountInfoByArticleId(Long articleId) {
        ArticleFootCountDTO count = queryArticleCountInfoCache(articleId);
        if (count == null) {
            count = userFootDao.countArticleByArticleId(articleId);
            if (count == null) {
                count = new ArticleFootCountDTO();
            }
            RedisClient.setStrWithExpire(buildKey(articleId), JSONUtil.toJsonStr(count), RedisConstants.ONE_DAY);
        }

        count.setCommentCount(commentReadService.queryCommentCount(articleId));
        return count;
    }

    private ArticleFootCountDTO queryArticleCountInfoCache(Long articleId) {
        String key = buildKey(articleId);
        String cache = RedisClient.getStr(key);
        if (StringUtils.isBlank(cache)) {
            return null;
        }

        return JSONUtil.toBean(cache, ArticleFootCountDTO.class);
    }

    @Override
    public void delArticleCountInfoCache(Long articleId) {
        log.debug("Del article count cache. articleId: {}", articleId);
        String key = buildKey(articleId);
        RedisClient.del(key);
    }

    public String buildKey(Long articleId) {
        return RedisConstants.REDIS_PRE_COUNT + articleId;
    }

    @Override
    public Integer queryCommentPraiseCount(Long commentId) {
        String key = RedisConstants.REDIS_PRE_COMMENT_PRAISE + commentId;
        String cache = RedisClient.getStr(key);
        if (!StringUtils.isBlank(cache)) {
            return Integer.parseInt(cache);
        }

        Integer praiseCount = userFootDao.countCommentPraise(commentId);
        RedisClient.setStrWithExpire(key, praiseCount.toString(), RedisConstants.ONE_DAY);
        return praiseCount;
    }

    @Override
    public void delCommentPraiseCount(Long commentId) {
        log.debug("Del comment praise cache. commentId: {}", commentId);
        String key = RedisConstants.REDIS_PRE_COMMENT_PRAISE + commentId;
        RedisClient.del(key);
    }
}
