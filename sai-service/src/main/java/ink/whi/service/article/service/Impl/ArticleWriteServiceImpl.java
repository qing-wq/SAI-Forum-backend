package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.core.image.service.ImageService;
import ink.whi.service.article.service.DraftsService;
import ink.whi.service.statistics.repo.dao.DictCommonDao;
import ink.whi.service.statistics.repo.entity.DictCommonDO;
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
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
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DraftsService draftsService;

    @Autowired
    private DictCommonDao dictCommonDao;

    /**
     * 文章发布
     *
     * @param articlePostReq
     * @return
     */
    @Override
    public Long saveArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        // 图片转链
        // todo: 耗时测试
        String content = imageService.mdImgReplace(articlePostReq.getContent());
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            //  article + article_detail + tag 三张表
            @Override
            public Long doInTransaction(TransactionStatus status) {
                // 删除草稿
                Long draftId = articlePostReq.getDraftId();
                draftsService.deleteDraft(draftId);

                if (!NumUtil.upZero(articlePostReq.getArticleId())) {
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
        // 是否开启审核
        article.setStatus(dictCommonDao.review() ? PushStatusEnum.REVIEW.getCode() : PushStatusEnum.ONLINE.getCode());
        articleDao.save(article);
        Long articleId = article.getId();

        // 保存文章内容
        articleDao.saveArticleContent(articleId, content);

        // 保存标签
        articleTagDao.insertBatch(articleId, tagIds, ArticleTypeEnum.BLOG);

        // 发布文章，阅读计数+1
        articleDao.incrReadCount(articleId);
        userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(), ReqInfoContext.getReqInfo().getUserId(), OperateTypeEnum.READ);

        // todo: 发布事件
        return articleId;
    }

    private void updateArticle(ArticleDO article, String content, Set<Long> tagIds) {
        // 文章是否处于未发布状态
        boolean unPublish = article.getStatus() != PushStatusEnum.ONLINE.getCode();
        Long articleId = article.getId();
        // 是否开启审核
        article.setStatus(dictCommonDao.review() ? PushStatusEnum.REVIEW.getCode() : PushStatusEnum.ONLINE.getCode());
//        article.setStatus(ArticleSettings.getReview() ? PushStatusEnum.REVIEW.getCode() : PushStatusEnum.ONLINE.getCode());

        // 更新文章、内容、标签
        articleDao.updateById(article);
        articleDao.updateArticleContent(articleId, content, unPublish);
        articleTagDao.updateTags(articleId, tagIds, ArticleTypeEnum.BLOG);
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO article = articleReadService.queryBasicArticle(articleId);

        if (!Objects.equals(article.getUserId(), ReqInfoContext.getReqInfo().getUserId())) {
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR_MIXED, articleId);
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                articleDao.deleteArticle(article);
            }
        });
    }
}
