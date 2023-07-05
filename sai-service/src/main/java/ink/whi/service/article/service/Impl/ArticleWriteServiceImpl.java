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
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
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

    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Override
    public Long saveArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        String content = imageService.mdImgReplace(articlePostReq.getContent());
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            //  article + article_detail + tag 三张表
            @Override
            public Long doInTransaction(TransactionStatus status) {
                if (NumUtil.upZero(article.getId())) {
                    return insertArticle(article, content, articlePostReq.getTagIds());
                } else {
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
        PushStatusEnum status = PushStatusEnum.formCode(article.getStatus());

        if (status == PushStatusEnum.ONLINE) {
            // 更新文章后重新进入审核状态
            article.setStatus(PushStatusEnum.REVIEW.getCode());
            articleDao.updateById(article);
            Long articleId = article.getId();

            articleDao.updateArticleContent(articleId, content, review);
            articleTagDao.updateTags(articleId, tagIds);

            article.setStatus(PushStatusEnum.REVIEW.getCode());
            articleDao.updateById(article);
        }
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
}
