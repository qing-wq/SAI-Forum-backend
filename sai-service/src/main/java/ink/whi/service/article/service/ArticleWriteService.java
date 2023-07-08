package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.article.req.DraftSaveReq;

/**
 * 文章写接口
 *
 * @author: qing
 * @Date: 2023/4/27
 */
public interface ArticleWriteService {
    Long saveArticle(ArticlePostReq articlePostReq);

    void deleteArticle(Long article);

    Long saveDraft(DraftSaveReq draftSaveReq);

    void deletedArticleDraft(Long articleId);
}
