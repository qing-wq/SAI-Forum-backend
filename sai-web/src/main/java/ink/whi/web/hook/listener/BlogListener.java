package ink.whi.web.hook.listener;

import ink.whi.api.model.constants.RankConstants;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.notify.service.NotifyMsgService;
import ink.whi.service.rank.RankService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.CountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static ink.whi.core.rabbitmq.BlogMqConstants.*;

/**
 * @author qing
 * @Date 2023/8/10
 */
@Slf4j
@Component
public class BlogListener {
    public static final String BLOG_PUBLISH_QUEUE = "blog.publish.queue";
    public static final String BLOG_PRAISE_QUEUE = "blog.praise.queue";
    public static final String BLOG_COLLECT_QUEUE = "blog.collect.queue";
    public static final String BLOG_COMMENT_QUEUE = "blog.comment.queue";
    public static final String BLOG_REPLY_QUEUE = "blog.reply.queue";
    public static final String BLOG_CANCEL_PRAISE_QUEUE = "blog.praise.cancel.queue";
    public static final String BLOG_CANCEL_COLLECT_QUEUE = "blog.collect.cancel.queue";
    public static final String SYSTEM_QUEUE = "system.queue";

    @Autowired
    private NotifyMsgService notifyService;

    @Autowired
    private RankService rankService;

    @Autowired
    private CountService countService;

    /**
     * 用户评论
     * @param comment
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_COMMENT_QUEUE),
            key = BLOG_COMMENT_KEY
    ))
    public void saveCommentNotify(CommentDO comment) {
        log.info("[INFO] 用户 {} 评论了文章 {}:{}", comment.getUserId(), comment.getArticleId(), comment.getContent());
        notifyService.saveCommentNotify(comment);
        countService.delCommentPraiseCount(comment.getId());
        rankService.incrementUserScore(comment.getUserId(), RankConstants.COMMENT_SCORE);
    }

    /**
     * 用户回复
     * @param comment
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_REPLY_QUEUE),
            key = BLOG_REPLY_KEY
    ))
    public void saveReplyNotify(CommentDO comment) {
        log.info("[用户 {} 回复了文章 {} ] {}", comment.getUserId(), comment.getArticleId(), comment.getContent());
        notifyService.saveReplyNotify(comment);
        countService.delCommentPraiseCount(comment.getId());
        rankService.incrementUserScore(comment.getUserId(), RankConstants.COMMENT_SCORE);
    }

    /**
     * 文章发布
     * @param
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_PUBLISH_QUEUE),
            key = BLOG_PUBLISH_KEY
    ))
    public void saveArticlePublish(Long userId) {
        log.info("[INFO] 用户 {} 发布了文章 {} ", userId);
        rankService.incrementUserScore(userId, RankConstants.PUBLISH_SCORE);
    }

    /**
     * 文章点赞
     * @param foot
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_PRAISE_QUEUE),
            key = BLOG_PRAISE_KEY
    ))
    public void saveArticlePraise(UserFootDO foot) {
        log.info("[INFO] 用户 {} 点赞了文章 {} ", foot.getUserId(), foot.getDocumentId());
        notifyService.saveArticlePraise(foot);
        // 2.删缓存
        countService.delArticleCountInfoCache(foot.getDocumentId());
        // fixme 应该点赞成功了才能加分，这里可能会有刷分的风险
        rankService.incrementUserScore(foot.getUserId(), RankConstants.PRAISE_SCORE);
    }

    /**
     * 文章收藏
     * @param foot
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_COLLECT_QUEUE),
            key = BLOG_COLLECT_KEY
    ))
    public void saveArticleCollect(UserFootDO foot) {
        log.info("[INFO] 用户 {} 收藏了文章 {} ", foot.getUserId(), foot.getDocumentId());
        notifyService.saveArticleCollect(foot);
        countService.delArticleCountInfoCache(foot.getDocumentId());
        rankService.incrementUserScore(foot.getUserId(), RankConstants.COLLECT_SCORE);
    }

    /**
     * 取消文章点赞
     * @param foot
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_CANCEL_PRAISE_QUEUE),
            key = BLOG_CANCEL_PRAISE_KEY
    ))
    public void removeArticlePraise(UserFootDO foot) {
        log.info("[INFO] 用户 {} 取消点赞 {} ", foot.getUserId(), foot.getDocumentId());
        notifyService.removeArticlePraise(foot);
        countService.delArticleCountInfoCache(foot.getDocumentId());
    }

    /**
     * 取消文章收藏
     * @param foot
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = BLOG_CANCEL_COLLECT_QUEUE),
            key = BLOG_CANCEL_COLLECT_KEY
    ))
    public void removeArticleCollect(UserFootDO foot) {
        log.info("[INFO] 用户 {} 取消收藏文章 {} ", foot.getUserId(), foot.getDocumentId());
        notifyService.removeArticleCollect(foot);
        countService.delArticleCountInfoCache(foot.getDocumentId());
    }

    /**
     * 用户注册
     * @param userId
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = BLOG_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            value = @Queue(name = SYSTEM_QUEUE),
            key = SYSTEM_KEY
    ))
    public void saveRegisterSystemNotify(Long userId) {
        log.info("[INFO] 用户 {} 注册", userId);
        notifyService.saveRegisterSystemNotify(userId);
    }
}
