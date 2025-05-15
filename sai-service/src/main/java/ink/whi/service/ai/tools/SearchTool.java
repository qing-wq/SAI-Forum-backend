package ink.whi.service.ai.tools;

import ink.whi.api.model.vo.article.dto.SimpleArticleDTO;
import ink.whi.service.article.service.ArticleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: qing
 * @Date: 2025/5/14
 */
@Component
public class SearchTool {

    @Autowired
    public ArticleReadService articleReadService;

    public List<SimpleArticleDTO> searchArticle(Map<String, String> params) {
        return articleReadService.queryArticleCluster(params);
    }
}
