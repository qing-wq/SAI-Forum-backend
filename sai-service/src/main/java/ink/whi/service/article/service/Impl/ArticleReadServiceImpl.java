package ink.whi.service.article.service.Impl;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONUtil;
import ink.whi.api.model.constants.RedisConstants;
import ink.whi.api.model.enums.*;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.dto.SimpleArticleDTO;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.cache.RedisClient;
import ink.whi.core.utils.ArticleUtil;
import ink.whi.core.utils.DateUtil;
import ink.whi.core.utils.MapUtils;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.dao.ArticleTagDao;
import ink.whi.service.article.repo.dao.DraftsDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleTagDO;
import ink.whi.service.article.repo.entity.DraftsDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Slf4j
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

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private DraftsDao draftsDao;

    @Override
//    @CachePut(key = "'articleList_' + #categoryId", cacheManager = "redisCacheManager", cacheNames = "article")
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
     *
     * @param userId
     * @return
     */
    @Override
    public Integer queryArticleCount(Long userId) {
        return articleDao.countArticleByUserId(userId);
    }

    /**
     * 查询文章信息
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDO queryBasicArticle(Long articleId) {
        return articleDao.getArticleById(articleId);
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
     *
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
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO queryDetailArticleInfo(Long articleId) {
        // 引入分布式锁
        String redisCacheKey = RedisConstants.REDIS_PRE_ARTICLE + articleId;
        String cache = RedisClient.getStr(redisCacheKey);
        ArticleDTO article = null;
        if (!ObjectUtils.isEmpty(cache)) {
            try {
                article = JSONUtil.toBean(cache, ArticleDTO.class);
            } catch (ConvertException e) {
                throw BusinessException.newInstance(StatusEnum.UNEXPECT_ERROR, "Json序列化失败！");
            }
        } else {
            // 缓存失效，抢分布式锁
            article = checkArticleByDBFour(articleId);
            if (article == null) {
                throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
            }
            // TODO 如果article=null，应该缓存空值?
            RedisClient.setStrWithExpire(redisCacheKey, JSONUtil.toJsonStr(article), 60L);
        }

        // 分类信息
        CategoryDTO category = article.getCategory();
        category.setCategory(categoryService.queryCategoryName(category.getCategoryId()));

        // 标签信息
        article.setTags(articleTagDao.listArticleTagsDetail(articleId));
        return article;
    }

    /**
     * Redis分布式锁第四种方法
     *
     * @param articleId
     * @return ArticleDTO
     */
    private ArticleDTO checkArticleByDBFour(Long articleId) {
        String redisLockKey = RedisConstants.REDIS_LOCK + articleId;
        RLock lock = redissonClient.getLock(redisLockKey);
        ArticleDTO article = null;
        try {
            //尝试加锁,最大等待时间3秒，上锁30秒自动解锁
            if (lock.tryLock(3, 30, TimeUnit.SECONDS)) {
                article = articleDao.queryArticleDetail(articleId);
            } else {
                // 未获得分布式锁线程睡眠一下；然后再去获取数据
                Thread.sleep(50);
                // 自旋
                this.queryDetailArticleInfo(articleId);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 防止锁的误释放
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return article;
    }

    /**
     * 查询用户主页展示文章
     *
     * @param userId
     * @param pageParam
     * @param select    用户选择标签
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
            // fixme：排序逻辑优化
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

    @Override
    public List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key) {
        // todo 当key为空时，返回热门推荐
        if (StringUtils.isBlank(key)) {
            return Collections.emptyList();
        }
        key = key.trim();
        List<ArticleDO> records = articleDao.listSimpleArticlesByBySearchKey(key);
        return records.stream().map(ArticleConverter::toSimpleDto)
                .collect(Collectors.toList());
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
        Map<Long, ArticleDO> articleDOMap = MapUtils.toMap(records, ArticleDO::getId, r -> r);
        articleIds.forEach(articleId -> {
            ArticleDO article = articleDOMap.get(articleId);
            if (article != null && article.getDeleted() == YesOrNoEnum.NO.getCode()) {
                articleDOS.add(article);
            }
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

    /**
     * 生成文章摘要
     *
     * @param content
     * @return
     */
    @Override
    public String generateSummary(String content) {
        return ArticleUtil.pickSummary(content);
    }

    /**
     * 获取已上线文章草稿
     *
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DraftsDTO getOnlineArticleDraft(Long articleId) {
        DraftsDO record = draftsDao.getArticleDraftByArticleId(articleId);

        if (record == null) {
            // 创建文章草稿
            ArticleDTO detail = articleDao.queryArticleDetail(articleId);
            record = ArticleConverter.toDrafts(detail);
            record.setDraftType(DraftsTypeEnum.ARTICLE);
            draftsDao.save(record);

            // 保存tags
            Set<Long> tagIds = articleTagDao.listArticleTagIds(articleId);
            articleTagDao.insertBatch(record.getId(), tagIds, ArticleTypeEnum.DRAFT);
        }

        Set<Long> tagIds = articleTagDao.listArticleTagIds(record.getId());
        DraftsDTO dto = ArticleConverter.toDraftsDTO(record);
        dto.setTagIds(tagIds);
        return dto;
    }

    @Override
    public DraftsDTO queryDraftById(Long draftId) {
        DraftsDO draft = draftsDao.getById(draftId);
        DraftsDTO dto = ArticleConverter.toDraftsDTO(draft);
        dto.setTagIds(articleTagDao.listArticleTagIds(draftId));
        return dto;
    }

    @Override
    public List<SimpleArticleDTO> queryArticleCluster(Map<String, String> params) {
        log.info("result from LLM: {}", params);
        List<ArticleDO> result = new ArrayList<>();
        if (!StringUtils.isBlank(params.get("keyword"))) {
            String keyword = params.get("keyword");
            List<ArticleDO> searchByTitle = articleDao.listSimpleArticlesByBySearchKey(keyword);
            result.addAll(searchByTitle);

            List<Long> searchByTags = articleTagDao.searchArticleByTags(keyword);
            List<ArticleDO> searchByTagsDto = searchByTags.stream().map(articleDao::getArticleById).toList();
            result.addAll(searchByTagsDto);
        }

        if (!StringUtils.isBlank(params.get("author"))) {
            String author = params.get("author");
            UserInfoDO user = userService.queryUserInfoByUserName(author);
            if (user != null) {
                result = result.stream().filter(s -> s.getUserId().equals(user.getId())).toList();
            }
        }

        if (!StringUtils.isBlank(params.get("startTime"))) {
            String startTimeStr = params.get("startTime");
            Date startTime = DateUtil.format(startTimeStr);
            result = result.stream().filter(s -> s.getCreateTime().after(startTime)).toList();
        }

        if (!StringUtils.isBlank(params.get("endTime"))) {
            String endTimeStr = params.get("endTime");
            Date endTime = DateUtil.format(endTimeStr);
            result = result.stream().filter(s -> s.getCreateTime().before(endTime)).toList();
        }

        return result.stream().map(ArticleConverter::toSimpleDto).toList();
    }
}
