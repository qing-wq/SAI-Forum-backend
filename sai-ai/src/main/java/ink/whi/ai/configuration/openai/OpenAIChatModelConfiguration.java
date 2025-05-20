package ink.whi.ai.configuration.openai;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

/**
 * @author: qing
 * @Date: 2024/8/8
 */
@Setter
@Getter
@Configuration
public class OpenAIChatModelConfiguration {

    @Autowired
    OpenAIProperties properties;

    @Bean
    ImageModel imageModel() {
        return OpenAiImageModel.builder()
                .apiKey(properties.getApiKey())
                .baseUrl(properties.getBaseUrl())
                .modelName("dall-e-2")
                .size("256x256")
                .build();
    }

    @Bean
    OpenAiChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(properties.getApiKey())
                .baseUrl(properties.getBaseUrl())
                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .modelName(properties.getModelName())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
