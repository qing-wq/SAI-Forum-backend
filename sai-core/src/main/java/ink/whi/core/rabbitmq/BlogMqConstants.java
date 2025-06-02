package ink.whi.core.rabbitmq;

/**
 * @author qing
 * @date 2023/7/31
 */
public class BlogMqConstants {

    /**
     * 交换机类型
     */
    public static final String BLOG_TOPIC_EXCHANGE = "blog.topic";

    public static final String BLOG_COMMENT_KEY = "blog.comment";
    public static final String BLOG_REPLY_KEY = "blog.reply";
    public static final String BLOG_PUBLISH_KEY = "blog.publish";
    public static final String BLOG_PRAISE_KEY = "blog.praise";
    public static final String BLOG_COLLECT_KEY = "blog.collect";
    public static final String BLOG_DELETE_COMMENT_KEY = "blog.comment.delete";
    public static final String BLOG_DELETE_REPLY_KEY = "blog.reply.delete";
    public static final String BLOG_CANCEL_PRAISE_KEY = "blog.praise.cancel";
    public static final String BLOG_CANCEL_COLLECT_KEY = "blog.collect.cancel";
    public static final String SYSTEM_KEY = "system";
}
