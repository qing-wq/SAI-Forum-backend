package ink.whi.service.ai.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @author: qing
 * @Date: 2024/8/9
 */
@Component
public class SummaryTools {

    private static final Pattern LINK_IMG_PATTERN = Pattern.compile("!?\\[(.*?)\\]\\((.*?)\\)");
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");

    public String pickSummary(String content) {
        if (StringUtils.isBlank(content)) {
            return StringUtils.EMPTY;
        }

        // 移除md的图片、超链
        content = content.replaceAll(LINK_IMG_PATTERN.pattern(), "");
        // 移除Html标签
        content = HTML_TAG_PATTERN.matcher(content).replaceAll("");
        return content.trim();
    }
}