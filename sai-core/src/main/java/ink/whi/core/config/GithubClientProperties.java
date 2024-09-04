package ink.whi.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2024/9/3
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "github")
public class GithubClientProperties {

    private String clientId;

    private String clientSecret;
}
