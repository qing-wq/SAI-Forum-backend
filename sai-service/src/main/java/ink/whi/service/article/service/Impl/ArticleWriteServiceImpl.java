package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.dao.DraftDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import ink.whi.service.article.repo.entity.DraftDO;
import ink.whi.service.article.repo.mapper.DraftMapper;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.core.image.service.ImageService;
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Set;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class ArticleWriteServiceImpl implements ArticleWriteService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleTagDao articleTagDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private DraftDao draftDao;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 未发布文章进行发布(insert)/ 已发布文章进行发布(update)
     *
     * @param articlePostReq
     * @return
     */
    @Override
    public Long saveArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        String content = imageService.mdImgReplace(articlePostReq.getContent());
        article.setStatus(PushStatusEnum.ONLINE.getCode());
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            //  article + article_detail + tag 三张表
            @Override
            public Long doInTransaction(TransactionStatus status) {
                if (NumUtil.upZero(article.getId())) {
                    // 新增文章记录
                    return insertArticle(article, content, articlePostReq.getTagIds());
                } else {
                    // 更新文章
                    ArticleDO record = articleDao.getById(article.getId());
                    if (!Objects.equals(record.getUserId(), article.getUserId())) {
                        throw BusinessException.newInstance(StatusEnum.FORBID_ERROR);
                    }
                    updateArticle(article, content, articlePostReq.getTagIds());
                    return article.getId();
                }
            }
        });
    }

    private Long insertArticle(ArticleDO article, String content, Set<Long> tagIds) {
        // todo: 增加白名单
        article.setStatus(PushStatusEnum.REVIEW.getCode());
        articleDao.save(article);
        Long articleId = article.getId();

        // 保存文章内容
        articleDao.saveArticleContent(articleId, content);

        // 保存标签
        articleTagDao.saveBatch(articleId, tagIds);

        // 发布文章，阅读计数+1
        articleDao.incrReadCount(articleId);
        userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(), ReqInfoContext.getReqInfo().getUserId(), OperateTypeEnum.READ);

        // todo: 发布事件
        return articleId;
    }

    private void updateArticle(ArticleDO article, String content, Set<Long> tagIds) {
        // 文章是否处于审核状态
        boolean review = article.getStatus() == PushStatusEnum.REVIEW.getCode();
        Long articleId = article.getId();

        // 更新文章、内容、标签
        article.setStatus(PushStatusEnum.REVIEW.getCode());
        articleDao.updateById(article);

        articleDao.updateArticleContent(articleId, content, review);
        articleTagDao.updateTags(articleId, tagIds);
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO article = articleReadService.queryBasicArticle(articleId);
        if (article == null) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS);
        }
        if (!Objects.equals(article.getUserId(), ReqInfoContext.getReqInfo().getUserId())) {
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR_MIXED, articleId);
        }
        article.setDeleted(YesOrNoEnum.YES.getCode());
        articleDao.updateById(article);
    }

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

        Long articleId = req.getArticleId();
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
}
