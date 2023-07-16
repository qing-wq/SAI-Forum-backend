package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.DraftDTO;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.core.image.service.ImageService;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.DraftDao;
import ink.whi.service.article.repo.entity.DraftDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/7/9
 */
@Service    
public class DraftServiceImpl implements DraftService {

    @Autowired
    private DraftDao draftDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ArticleReadService articleReadService;

    /**
     * 创建草稿
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDraft(DraftSaveReq req) {
        DraftDO draft = ArticleConverter.toDraftDo(req, ReqInfoContext.getReqInfo().getUserId());
        String content = imageService.mdImgReplace(req.getContent());
        draft.setContent(content);

        if (NumUtil.upZero(req.getArticleId())) {
            // 新增未发布文章草稿
            draftDao.save(draft);
            return draft.getId();
        }

        return saveArticleDraft(draft, req.getArticleId());
    }

    /**
     * 保存已发布文章草稿
     *
     * @param draft
     * @param articleId
     * @return
     */
    public Long saveArticleDraft(DraftDO draft, Long articleId) {
        // 新增发布文章草稿
        DraftDO draftRecord = draftDao.findLastDraft(articleId);
        if (draftRecord == null) {
            // 不存在则创建一个
            draftDao.save(draft);
            return draft.getId();
        }
        // 存在则直接更新记录
        draft.setId(draftRecord.getId());
        draftDao.updateById(draft);
        return draft.getId();
    }

    /**
     * 删除文章草稿记录
     * @param articleId
     */
    @Override
    public void deletedArticleDraft(Long articleId) {
        draftDao.deletedArticleDraft(articleId);
    }

    /**
     * 获取草稿箱列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    @Override
    public PageListVo<DraftDTO> listDrafts(Long userId, PageParam pageParam) {
        return draftDao.listDrafts(userId, pageParam);
    }

    /**
     * 查询草稿记录
     *
     * @param draftId
     * @return
     */
    @Override
    public DraftDTO queryDraftById(Long draftId) {
        DraftDTO dto = draftDao.queryDraftById(draftId);
        if (dto == null) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, draftId);
        }
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(userId, dto.getAuthor())) {
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR_MIXED, "您没有权限查看");
        }
        return dto;
    }

    /**
     * 获取文章草稿
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO queryDraftByArticleId(Long articleId) {
        ArticleDTO detail = articleReadService.queryDetailArticleInfo(articleId);
        if (detail == null) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, articleId);
        }
        if (!Objects.equals(detail.getAuthor(), ReqInfoContext.getReqInfo().getUserId())) {
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR_MIXED, articleId);
        }
        DraftDO draft = draftDao.findLastDraft(articleId);
        if (draft != null) {
            // 查询草稿中是否有记录
            detail.setContent(draft.getContent());
            detail.setTitle(draft.getTitle());
        }
        return detail;
    }

}
