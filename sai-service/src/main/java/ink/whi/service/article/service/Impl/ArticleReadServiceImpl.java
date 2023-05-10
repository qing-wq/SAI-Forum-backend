package ink.whi.service.article.service.Impl;

import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.utils.MapUtils;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleTagDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    public Map<Long, Long> queryArticleCountsByCategory() {
        return articleDao.countArticleByCategoryId();
    }

    /**
     * 查询作者文章数量
     * @param userId
     * @return
     */
    @Override
    public Integer queryArticleCount(Long userId) {
        return articleDao.countArticleByUserId(userId);
    }

    /**
     * 查询文章信息
     * @param articleId
     * @return
     */
    @Override
    public ArticleDO queryBasicArticle(Long articleId) {
        return articleDao.getById(articleId);
    }

    /**
     * 关联文章推荐（标签|阅读量）
     *
     * @param articleId
     * @param pageParam
     * @return
     */
    @Override
    public PageListVo<ArticleDTO> queryRecommendArticle(Long articleId, PageParam pageParam) {
        ArticleDO article = queryBasicArticle(articleId);
        if (article == null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在");
        }
        List<ArticleTagDO> tags = articleTagDao.listArticleTags(articleId);
        List<Long> tagIds = tags.stream().map(ArticleTagDO::getTagId).toList();
        List<ArticleDO> list = articleDao.listRelatedArticlesOrderByReadCount(article.getCategoryId(), tagIds, pageParam);
        // 从列表中移除当前文章
        if (list.removeIf(s -> s.getId().equals(articleId))) {
            pageParam.setPageSize(pageParam.getPageSize() - 1);
        }
        return buildArticleListVo(list, pageParam.getPageSize());
    }

    /**
     * 查询文章总信息，用于文章详情页文章信息展示
     * @param articleId
     * @param readUser
     * @return
     */
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
        article.setPraisedUsers(userFootService.queryArticlePraisedUsers(articleId));
        return article;
    }

    /**
     * 查询文章详细信息，主要用于文章列表展示信息
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO queryDetailArticleInfo(Long articleId) {
        ArticleDTO article = articleDao.queryArticleDetail(articleId);
        if (article == null) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }
        // 分类信息
        CategoryDTO category = article.getCategory();
        category.setCategory(categoryService.queryCategoryName(category.getCategoryId()));

        // 标签信息
        article.setTags(articleTagDao.listArticleTagsDetail(articleId));
        return article;
    }

    /**
     * 查询用户主页展示文章
     * @param userId
     * @param pageParam
     * @param select 用户选择标签
     * @return
     */
    @Override
    public PageListVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select) {
        List<ArticleDO> records = null;
        if (select == HomeSelectEnum.ARTICLE) {
            // 用户的文章列表
            records = articleDao.listArticlesByUserId(userId, pageParam);
        } else if (select == HomeSelectEnum.READ) {
            // 用户的阅读记录
            List<Long> articleIds = userFootService.queryUserReadArticleList(userId, pageParam);
            records = CollectionUtils.isEmpty(articleIds) ? Collections.emptyList() : articleDao.listByIds(articleIds);
            records = sortByIds(articleIds, records);
        } else if (select == HomeSelectEnum.COLLECTION) {
            // 用户的收藏列表
            List<Long> articleIds = userFootService.queryUserCollectionArticleList(userId, pageParam);
            records = CollectionUtils.isEmpty(articleIds) ? Collections.emptyList() : articleDao.listByIds(articleIds);
            records = sortByIds(articleIds, records);
        }

        if (CollectionUtils.isEmpty(records)) {
            return PageListVo.emptyVo();
        }
        return buildArticleListVo(records, pageParam.getPageSize());
    }

    /**
     * 排序
     *
     * @param articleIds
     * @param records
     * @return
     */
    private List<ArticleDO> sortByIds(List<Long> articleIds, List<ArticleDO> records) {
        List<ArticleDO> articleDOS = new ArrayList<>();
        Map<Long, ArticleDO> articleDOMap = MapUtils.toMap(articleDOS, ArticleDO::getId, r -> r);
        articleIds.forEach(articleId -> {
            ArticleDO article = articleDOMap.get(articleId);
            Optional.ofNullable(article).ifPresent(articleDOS::add);
        });
        return articleDOS;
    }

    @Override
    public PageListVo<ArticleDTO> buildArticleListVo(List<ArticleDO> records, long pageSize) {
        List<ArticleDTO> result = records.stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageListVo.newVo(result, pageSize);
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
        dto.setTags(articleTagDao.listArticleTagsDetail(record.getId()));
        // 阅读计数统计
        dto.setCount(countService.queryArticleCountInfoByArticleId(record.getId()));
        // 作者信息
        BaseUserInfoDTO author = userService.queryBasicUserInfo(dto.getAuthor());
        dto.setAuthorName(author.getUserName());
        dto.setAuthorAvatar(author.getPhoto());
        return dto;
    }
}
