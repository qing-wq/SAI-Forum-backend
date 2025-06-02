package ink.whi.api.model.constants;

/**
 * @author: qing
 * @Date: 2025/5/22
 */
public class RankConstants {
    
    // 不同行为的分数权重
    public static final double INIT_ARTICLE_SCORE = 3.0;  // 初始状态文章权重
    public static final double PRAISE_SCORE = 1.0;  // 点赞权重
    public static final double COLLECT_SCORE = 1.0; // 收藏权重
    public static final double COMMENT_SCORE = 3.0; // 评论权重
    public static final double READ_SCORE = 0.1;    // 阅读权重
    public static final double FOLLOW_SCORE = 1.0;  // 关注权重
    public static final double PUBLISH_SCORE = 10.0;    // 新发布文章权重

}
