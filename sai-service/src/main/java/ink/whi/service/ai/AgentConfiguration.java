package ink.whi.service.ai;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import ink.whi.core.utils.JsonUtil;
import ink.whi.service.ai.agent.AISearchHelper;
import ink.whi.service.ai.agent.AISummaryHelper;
import ink.whi.service.ai.tools.SearchTool;
import ink.whi.service.ai.tools.SummaryTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * @author: qing
 * @Date: 2025/5/14
 */
@Configuration
public class AgentConfiguration {

    @Autowired
    private SearchTool searchTool;

    @Autowired
    private SummaryTools summaryTools;

    @Bean
    public AISummaryHelper summaryHelper(OpenAiChatModel chatModel) {

        // 创建移除文本中所有的MarkDown格式的图片，链接的工具
        ToolSpecification pickSummaryToolSpec = ToolSpecification.builder()
                .name("pickSummary")
                .description("移除文本中所有的MarkDown格式的图片，链接")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("content", "需要处理的文本内容")
                        .required("content")
                        .build())
                .build();

        ToolExecutor pickSummaryToolExecutor = (request, memoryId) -> {
            Map<String, Object> arguments = JsonUtil.toObj(request.arguments(), Map.class);
            String content = arguments.get("content").toString();
            return summaryTools.pickSummary(content);
        };

        return AiServices.builder(AISummaryHelper.class)
                .chatModel(chatModel)
                .tools(Map.of(pickSummaryToolSpec, pickSummaryToolExecutor))
                .build();
    }

    @Bean
    public AISearchHelper searchHelper(OpenAiChatModel chatModel) {
        // 创建文章搜索工具
//        ToolSpecification searchArticleToolSpec = ToolSpecification.builder()
//                .name("searchArticle")
//                .description("根据关键词、作者、时间范围搜索文章")
//                .parameters(JsonObjectSchema.builder()
//                        .addStringProperty("keyword", "搜索关键词")
//                        .addStringProperty("author", "文章作者")
//                        .addStringProperty("startTime", "开始时间，格式为yyyy-MM-dd")
//                        .addStringProperty("endTime", "结束时间，格式为yyyy-MM-dd")
//                        .build())
//                .build();
//
//        ToolExecutor searchArticleToolExecutor = (request, memoryId) -> {
//            Map<String, String> arguments = JsonUtil.toObj(request.arguments(), Map.class);
//            String keyword = arguments.getOrDefault("keyword", "");
//            String author = arguments.getOrDefault("author", "");
//            String startTimeStr = arguments.getOrDefault("startTime", null);
//            String endTimeStr = arguments.getOrDefault("endTime", null);
//
//            // 简化处理，实际应用中应该有更好的日期解析
//            Date startTime = DateUtil.format(startTimeStr);
//            Date endTime = DateUtil.format(endTimeStr);
//
//            List<SimpleArticleDTO> articles = searchTool.searchArticle(keyword, author, startTime, endTime);
//            return JsonUtil.toStr(articles);
//        };

        return AiServices.builder(AISearchHelper.class)
                .chatModel(chatModel)
//                .tools(Map.of(searchArticleToolSpec, searchArticleToolExecutor))
                .build();
    }
}
