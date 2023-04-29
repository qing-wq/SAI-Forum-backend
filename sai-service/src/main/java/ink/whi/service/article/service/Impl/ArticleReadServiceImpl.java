package ink.whi.service.article.service.Impl;

import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class ArticleReadServiceImpl implements ArticleReadService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTagDao articleTagDao;

    @Autowired
    private CountService countService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFootService userFootService;

    @Override
    public PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam pageParam) {
        List<ArticleDO> list = articleDao.listArticleByCategoryId(categoryId, pageParam);
        return buildArticleListVo(list, pageParam.getPageSize());
    }

    @Override
    public PageListVo<ArticleDTO> buildArticleListVo(List<ArticleDO> records, long pageSize) {
        List<ArticleDTO> result = records.stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageListVo.newVo(result, pageSize);
    }

    @Override
    public Map<Long, Long> queryArticleCountsByCategory() {
        return articleDao.countArticleByCategoryId();
    }

    @Override
    public ArticleDTO queryTotalArticleInfo(Long articleId, Long readUser) {
        ArticleDTO article = queryDetailArticleInfo(articleId);

        // 文章阅读计数+1
        articleDao.incrReadCount(articleId);

        // 文章操作
        if (readUser != null) {
            UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getAuthor(), readUser, OperateTypeEnum.READ);
            article.setPraised(Objects.equals(foot.getPraiseStat(), PraiseStatEnum.PRAISE.getCode()));
            article.setCommented(Objects.equals(foot.getCommentStat(), CommentStatEnum.COMMENT.getCode()));
            article.setCollected(Objects.equals(foot.getCollectionStat(), CollectionStatEnum.COLLECTION.getCode()));
        } else {
            // 未登录，全部设置为未处理
            article.setPraised(false);
            article.setCommented(false);
            article.setCollected(false);
        }

        // 设置文章的点赞|收藏|阅读数
        article.setCount(countService.queryArticleCountInfoByArticleId(articleId));

        // 设置文章的点赞列表
//        article.setPraisedUsers(userFootService.queryArticlePraisedUsers(articleId));
        return article;
    }

    @Override
    public Integer queryArticleCount(Long userId) {
        return articleDao.countArticleByUserId(userId);
    }

    @Override
    public ArticleDO queryBasicArticle(Long articleId) {
        return articleDao.getById(articleId);
    }

    private ArticleDTO queryDetailArticleInfo(Long articleId) {
        ArticleDTO article = articleDao.queryArticleDetail(articleId);
        if (article == null) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }
        // 更新分类相关信息
        CategoryDTO category = article.getCategory();
        category.setCategory(categoryService.queryCategoryName(category.getCategoryId()));

        // 更新标签信息
        article.setTags(articleTagDao.queryArticleTagDetails(articleId));
        return article;
    }

    /**
     * 补全文章的阅读计数、作者、分类、标签等信息
     *
     * @param record
     * @return
     */
    private ArticleDTO fillArticleRelatedInfo(ArticleDO record) {
        ArticleDTO dto = ArticleConverter.toDto(record);
        // 分类信息
        dto.getCategory().setCategory(categoryService.queryCategoryName(record.getCategoryId()));
        // 标签列表
        dto.setTags(articleTagDao.queryArticleTagDetails(record.getId()));
        // 阅读计数统计
        dto.setCount(countService.queryArticleCountInfoByArticleId(record.getId()));
        // 作者信息
        BaseUserInfoDTO author = userService.queryBasicUserInfo(dto.getAuthor());
        dto.setAuthorName(author.getUserName());
        dto.setAuthorAvatar(author.getPhoto());
        return dto;
    }
}
