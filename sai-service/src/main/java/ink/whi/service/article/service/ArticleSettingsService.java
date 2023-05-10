package ink.whi.service.article.service;

import ink.whi.api.model.enums.OperateArticleEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
public interface ArticleSettingsService {
    Integer getArticleCount();

    void updateArticle(ArticlePostReq req);

    void operateArticle(Long articleId, OperateArticleEnum operate);

    void deleteArticle(Long articleId);

    PageVo<ArticleDTO> getArticleList(PageParam pageParam);
}
