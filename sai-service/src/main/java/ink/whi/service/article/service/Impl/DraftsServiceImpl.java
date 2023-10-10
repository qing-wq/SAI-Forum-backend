package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.req.DraftsSaveReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.dao.DraftsDao;
import ink.whi.service.article.repo.entity.DraftsDO;
import ink.whi.service.article.service.DraftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/10/10
 */
@Service
public class DraftsServiceImpl implements DraftsService {

    @Autowired
    private DraftsDao draftsDao;

    @Autowired
    private ArticleTagDao articleTagDao;

    /**
     * 初始化草稿
     * @param draftsSaveReq
     * @return
     */
    @Override
    public Long initDraft(DraftsSaveReq draftsSaveReq) {
        DraftsDO draft = ArticleConverter.toDraftsDO(draftsSaveReq, ReqInfoContext.getReqInfo().getUserId());
        draftsDao.save(draft);
        return draft.getId();
    }

    @Override
    public void deleteDraft(Long draftId) {
        DraftsDO dto = draftsDao.getById(draftId);
        if (dto == null || Objects.equals(dto.getDeleted(), YesOrNoEnum.YES.getCode())) {
            return;
        }
        dto.setDeleted(YesOrNoEnum.YES.getCode());
        draftsDao.updateById(dto);
    }

    @Override
    public PageListVo<DraftsDTO> listDraft(Long userId, PageParam pageParam) {
        List<DraftsDO> list = draftsDao.listDraftByUserId(userId, pageParam);
        return PageListVo.newVo(ArticleConverter.toDraftList(list), pageParam.getPageSize());
    }

    /**
     * 更新草稿
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDraft(DraftsSaveReq req) {
        Long authorId = ReqInfoContext.getReqInfo().getUserId();
        Long draftId = req.getDraftId();
        DraftsDO draft = ArticleConverter.toDraftsDO(req, authorId);

        // 保存草稿内容
        draftsDao.updateById(draft);

        // 草稿标签
        articleTagDao.updateTags(draftId, req.getTagIds(), ArticleTypeEnum.DRAFT);
    }
}
