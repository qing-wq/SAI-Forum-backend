package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.req.DraftsSaveReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: qing
 * @Date: 2023/10/10
 */
public interface DraftsService {
    Long initDraft(DraftsSaveReq draftsSaveReq);

    void deleteDraft(Long draftId);

    PageListVo<DraftsDTO> listDraft(Long userId, PageParam pageParam);

    @Transactional(rollbackFor = Exception.class)
    void updateDraft(DraftsSaveReq req);
}
