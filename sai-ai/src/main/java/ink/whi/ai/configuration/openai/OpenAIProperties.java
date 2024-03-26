package ink.whi.ai.configuration.openai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2024/8/8
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "openai")
public class OpenAIProperties {

    private String apiKey;

    private String modelName;

    private String baseUrl;
}
