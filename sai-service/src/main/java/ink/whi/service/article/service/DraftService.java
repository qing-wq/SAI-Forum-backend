package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.DraftDTO;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: qing
 * @Date: 2023/7/9
 */
public interface DraftService {
    Long saveDraft(DraftSaveReq req);

    void deletedArticleDraft(Long articleId);

    PageListVo<DraftDTO> listDrafts(Long userId, PageParam pageParam);

    DraftDTO queryDraftById(Long draftId);

    ArticleDTO queryDraftByArticleId(Long articleId);
}
