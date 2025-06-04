package ink.whi.service.ai.agent;

import dev.langchain4j.service.SystemMessage;

import java.util.Map;

/**
 * @author: qing
 * @Date: 2025/5/14
 */
public interface AISearchHelper {
    @SystemMessage("""
           请你作为一个文章搜索专家，负责根据用户描述识别搜索条件，如文章关键词、作者、时间等参数，返回值为Json格式，示例:
           {"keyword":"分布式","author":"管理员","startTime":"2025-05-01","endTime":""},
           没有的参数留空即可，参数类型都是字符串
            """)
    Map<String, String> chat(String message);
}
