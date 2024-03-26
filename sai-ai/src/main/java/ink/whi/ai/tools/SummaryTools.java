package ink.whi.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author: qing
 * @Date: 2024/8/9
 */
@Component
public class SummaryTools {

    @Tool
     String currentTime() {
        return LocalTime.now().toString();
    }
}