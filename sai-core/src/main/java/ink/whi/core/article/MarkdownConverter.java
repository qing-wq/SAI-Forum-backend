package ink.whi.core.article;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public class MarkdownConverter {
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Document document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
