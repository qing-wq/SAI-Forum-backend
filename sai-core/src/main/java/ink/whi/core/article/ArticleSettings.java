package ink.whi.core.article;

/**
 * @author: qing
 * @Date: 2023/10/4
 */
public class ArticleSettings {
    /**
     * 默认开启审核
     */
    public static Boolean REVIEW = true;

    public static synchronized void enable(Boolean enable) {
        REVIEW = enable;
    }

    public static synchronized Boolean getReview() {
        return REVIEW;
    }
}
