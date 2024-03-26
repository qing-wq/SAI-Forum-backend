package ink.whi.ai.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * @author: qing
 * @Date: 2024/8/9
 */
@AiService(chatModel = "OpenAiChatModel")
public interface AISummaryHelper {

    @SystemMessage("""
            请你作为一个摘要编写专家，请在遵守这些准则的同时，对提供的文本进行简洁而全面的摘要：
                - 制作一个详细，详尽，深入且复杂的摘要，同时保持清晰度和简洁性。
                - 结合主要思想和基本信息，消除外科语言并关注关键方面。
                - 严格依靠提供的文本，而不包括外部信息。
                - 以段落形式格式化摘要，以便于理解，开头应使用「本文介绍了」、「本文分享了」等等标语开头。
                - 尽量用三句话200字以内完成对文章摘要的编写。
            """)
    String chat(String message);
}
