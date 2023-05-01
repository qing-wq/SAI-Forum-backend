package ink.whi.service.article.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.vo.article.rep.ArticlePostReq;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.core.image.service.ImageService;
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    private ImageService imageService;

    @Autowired
    private UserFootService userFootService;

    @Override
    public Long saveArticle(ArticlePostReq articlePostReq) {
        ArticleDO article = ArticleConverter.toArticleDo(articlePostReq, ReqInfoContext.getReqInfo().getUserId());
        String content = articlePostReq.getContent();
        // todo: 上传图片
        if (NumUtil.upZero(article.getId())) {
            return insertArticle(article, content, articlePostReq.getTagIds());
        } else {
            updateArticle(article, content, articlePostReq.getTagIds());
            return article.getId();
        }
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
        // 若文章处于审核状态，则直接更新上一条记录；否则新插入一条记录
        boolean review = article.getStatus().equals(PushStatusEnum.REVIEW.getCode());
        // todo：增加白名单
        article.setStatus(PushStatusEnum.REVIEW.getCode());
        articleDao.updateById(article);
        Long articleId = article.getId();

        articleDao.updateArticleContent(articleId, content, review);
        articleTagDao.updateTags(articleId, tagIds);

        article.setStatus(PushStatusEnum.REVIEW.getCode());
        articleDao.updateById(article);
    }

}
