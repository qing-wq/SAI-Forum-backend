package ink.whi.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/10/4
 */
public class ArticleProperties {
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
