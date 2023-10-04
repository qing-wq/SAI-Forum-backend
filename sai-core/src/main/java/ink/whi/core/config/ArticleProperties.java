package ink.whi.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/10/4
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "article")
public class ArticleProperties {
    /**
     * 是否开启审核
     */
    private Boolean review;
}
