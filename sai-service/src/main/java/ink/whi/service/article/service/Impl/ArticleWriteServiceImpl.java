package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.core.config.ArticleProperties;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.core.image.service.ImageService;
import ink.whi.service.user.service.UserFootService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
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
    private ArticleProperties articleProperties;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 文章发布
     *
     * @param articlePostReq
     * @return
     */
    @Override
    public Long saveArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        String content = imageService.mdImgReplace(articlePostReq.getContent());
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            //  article + article_detail + tag 三张表
            @Override
            public Long doInTransaction(TransactionStatus status) {
                if (!NumUtil.upZero(articlePostReq.getArticleId())) {
                    // fixme: 新增文章草稿后，发布文章携带id，不会进到这
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
        articleTagDao.insertBatch(articleId, tagIds);

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
        article.setStatus(articleProperties.getReview() ? PushStatusEnum.REVIEW.getCode() : PushStatusEnum.ONLINE.getCode());

        // 更新文章、内容、标签
        articleDao.updateById(article);
        articleDao.updateArticleContent(articleId, content, unPublish);
        articleTagDao.updateTags(articleId, tagIds);
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

    /**
     * 更新草稿
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDraft(ArticlePostReq req) {
        Long authorId = ReqInfoContext.getReqInfo().getUserId();
        Long articleId = req.getArticleId();
        ArticleDO article = ArticleConverter.toArticleDo(req, authorId);

        // 存草稿
        article.setStatus(PushStatusEnum.OFFLINE.getCode());
        articleDao.updateById(article);
        articleDao.updateArticleContent(articleId, req.getContent(), true);
        articleTagDao.updateTags(articleId, req.getTagIds());
    }

    @Override
    public Long initArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        // 存草稿
        article.setStatus(PushStatusEnum.OFFLINE.getCode());
        articleDao.save(article);
        articleDao.saveArticleContent(article.getId(), "");
        return article.getId();
    }

    /**
     * 更新已发布文章副本
     *
     * @param articlePostReq
     */
    @Override
    public void updateArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        String content = imageService.mdImgReplace(articlePostReq.getContent());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            //  article + article_detail + tag 三张表
            @Override
            public void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                // 更新文章
                ArticleDO record = articleDao.getById(article.getId());
                if (!Objects.equals(record.getUserId(), article.getUserId())) {
                    throw BusinessException.newInstance(StatusEnum.FORBID_ERROR);
                }
                Long articleId = article.getId();
                articleDao.updateArticleCopy(articleId, content);
            }
        });
    }
}
