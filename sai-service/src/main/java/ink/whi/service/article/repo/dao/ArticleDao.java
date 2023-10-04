package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.google.common.collect.Maps;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.YearArticleDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.help.ArticleHelper;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import ink.whi.service.article.repo.entity.DraftDO;
import ink.whi.service.article.repo.entity.ReadCountDO;
import ink.whi.service.article.repo.mapper.ArticleDetailMapper;
import ink.whi.service.article.repo.mapper.ArticleMapper;
import ink.whi.service.article.repo.mapper.ReadCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    @Autowired
    private ReadCountMapper readCountMapper;

    @Autowired
    private ArticleTagDao articleTagDao;

    /**
     * 查询文章详情
     *
     * @param articleId
     * @return
     */
    public ArticleDTO queryArticleDetail(Long articleId) {
        // 查询文章记录
        ArticleDO article = baseMapper.selectById(articleId);
        if (article == null || Objects.equals(article.getDeleted(), YesOrNoEnum.YES.getCode())) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }

        // 查询文章正文
        ArticleDTO dto = ArticleConverter.toDto(article);
        if (ArticleHelper.showContent(article)) {
            ArticleDetailDO detail = findLatestDetail(articleId);
            dto.setContent(detail.getContent());
        } else {
            // 对于审核中的文章，只有作者本人才能看到原文
            dto.setContent("### 文章审核中，请稍后再看");
        }
        return dto;
    }

    /**
     * 查询分类对应文章内容
     *
     * @param categoryId
     * @param pageParam
     * @return
     */
    public List<ArticleDO> listArticleByCategoryId(Long categoryId, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        boolean hasCategory = true;
        // 分类不存在时，查询所有
        if (categoryId == null || categoryId <= 0) {
            hasCategory = false;
        }
        query.eq(hasCategory, ArticleDO::getCategoryId, categoryId)
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .orderByDesc(ArticleDO::getToppingStat, ArticleDO::getCreateTime)
                .last(PageParam.getLimitSql(pageParam));
        return baseMapper.selectList(query);
    }

    /**
     * 查询分类对应文章数
     * key-categoryId, value-count
     *
     * @return
     */
    public Map<Long, Long> countArticleByCategoryId() {
        QueryWrapper<ArticleDO> wrapper = Wrappers.query();
        wrapper.select("count(*) as cnt, category_id")
                .eq("status", PushStatusEnum.ONLINE.getCode())
                .eq("deleted", YesOrNoEnum.NO.getCode())
                .groupBy("category_id");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(wrapper);
        Map<Long, Long> map = Maps.newHashMapWithExpectedSize(mapList.size());
        mapList.forEach(s -> {
            Long cnt = (Long) s.get("cnt");
            if (cnt != null && cnt > 0) {
                map.put((Long) s.get("category_id"), cnt);
            }
        });
        return map;
    }

    /**
     * 阅读计数+1
     *
     * @param articleId
     */
    public void incrReadCount(Long articleId) {
        LambdaQueryWrapper<ReadCountDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReadCountDO::getDocumentId, articleId)
                .eq(ReadCountDO::getDocumentType, DocumentTypeEnum.ARTICLE.getCode());
        ReadCountDO cnt = readCountMapper.selectOne(wrapper);
        if (cnt == null) {
            cnt = new ReadCountDO()
                    .setDocumentId(articleId)
                    .setDocumentType(DocumentTypeEnum.ARTICLE.getCode())
                    .setCnt(1);
            readCountMapper.insert(cnt);
            return;
        }
        // todo: 存在并发覆盖问题？
        cnt.setCnt(cnt.getCnt() + 1);
        readCountMapper.updateById(cnt);
    }

    public Integer countArticleByUserId(Long userId) {
        return lambdaQuery().eq(ArticleDO::getUserId, userId)
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .count().intValue();
    }

    public List<YearArticleDTO> listYearArticleByUserId(Long userId) {
        return baseMapper.listYearArticleByUserId(userId);
    }

    /**
     * 根据分类+标签查询文章，并按阅读数排序
     *
     * @param categoryId
     * @param tagIds
     * @param page
     * @return
     */
    public List<ArticleDO> listRelatedArticlesOrderByReadCount(Long categoryId, List<Long> tagIds, PageParam page) {
        List<ReadCountDO> readCount = baseMapper.listArticleByCategoryAndTags(categoryId, tagIds, page);
        if (CollectionUtils.isEmpty(readCount)) {
            return Collections.emptyList();
        }
        List<Long> articleIds = readCount.stream().map(ReadCountDO::getDocumentId).toList();
        List<ArticleDO> article = baseMapper.selectBatchIds(articleIds);
        article.sort((o1, o2) -> {
            int i = articleIds.indexOf(o1.getId());
            int j = articleIds.indexOf(o2.getId());
            return Integer.compare(i, j);
        });
        return article;
    }

    public void saveArticleContent(Long articleId, String content) {
        ArticleDetailDO detail = ArticleDetailDO.builder().articleId(articleId).content(content).version(1L).deleted(YesOrNoEnum.NO.getCode()).build();
        articleDetailMapper.insert(detail);
    }

    /**
     * 若文章处于审核状态，则直接更新上一条记录；
     * 对于已发布的文章，新插入一条记录，并将版本号+1
     * 即对于审核之前的内容不保存历史版本，上线后的文章保存历史版本
     *
     * @param articleId
     * @param content
     * @param unPublish
     */
    public void updateArticleContent(Long articleId, String content, boolean unPublish) {
        if (content == null) {
            return;
        }

        if (unPublish) {
            articleDetailMapper.updateContent(articleId, content);
        } else {
            ArticleDetailDO detail = findLatestDetail(articleId);
            detail.setVersion(detail.getVersion() + 1);
            detail.setContent(content);
            detail.setId(null);
            articleDetailMapper.insert(detail);
        }
    }

    public List<ArticleDO> listArticlesByUserId(Long userId, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getUserId, userId)
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        if (!Objects.equals(ReqInfoContext.getReqInfo().getUserId(), userId)) {
            // 作者本人，可以查看审核、上线文章；其他用户，只能查看上线的文章
            query.eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());
        } else {
            query.in(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode(), PushStatusEnum.REVIEW.getCode());
        }
        return baseMapper.selectList(query);
    }

    public Integer countArticle() {
        return lambdaQuery().eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }

    public List<ArticleDO> listArticles(PageParam pageParam) {
        return lambdaQuery().eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam)).list();
    }

    public List<ArticleDO> listSimpleArticlesByBySearchKey(String key) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ArticleDO::getTitle, key)
                                .or()
                                .like(ArticleDO::getShortTitle, key)
                );
        return list(query);
    }

    /**
     * 查询文章内容
     *
     * @param articleId
     * @return
     */
    public ArticleDetailDO findLatestDetail(long articleId) {
        // 查询文章内容
        return ChainWrappers.lambdaQueryChain(articleDetailMapper)
                .eq(ArticleDetailDO::getArticleId, articleId)
                .eq(ArticleDetailDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(ArticleDetailDO::getVersion)
                .one();
    }

    public void updateDraft(DraftDO draft) {
        articleDetailMapper.updateContent(draft.getId(), draft.getContent());
    }

    public List<ArticleDO> listDrafts(Long userId, PageParam pageParam) {
        return lambdaQuery().eq(ArticleDO::getUserId, userId)
                .eq(ArticleDO::getStatus, PushStatusEnum.OFFLINE.getCode())
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getCreateTime)
                .list();
    }

    public void deleteArticle(ArticleDO article) {
        article.setDeleted(YesOrNoEnum.YES.getCode());
        updateById(article);
        Long articleId = article.getId();

        // 删除文章内容
        LambdaUpdateChainWrapper<ArticleDetailDO> chainWrapper = ChainWrappers.lambdaUpdateChain(articleDetailMapper);
        chainWrapper.eq(ArticleDetailDO::getArticleId, articleId)
                .eq(ArticleDetailDO::getDeleted, YesOrNoEnum.NO.getCode())
                .set(ArticleDetailDO::getDeleted, YesOrNoEnum.YES.getCode())
                .update();

        // 删除文章标签
        articleTagDao.deleteArticleTags(articleId);
    }

    /**
     * 获取上线文章的草稿
     *
     * @param articleId
     * @return
     */
    public ArticleDTO getArticleDraft(Long articleId) {
        // 查询文章记录
        ArticleDO article = getArticleById(articleId);

        // 查询文章正文
        ArticleDTO dto = ArticleConverter.toDto(article);
        ArticleDetailDO detail = findLatestDetail(articleId);
        if (ArticleHelper.isOnline(article) && detail.getCopy() != null) {
            // 如果是上线文章且存在副本，就返回副本
            dto.setContent(detail.getCopy());
        } else {
            dto.setContent(detail.getContent());
        }
        return dto;
    }

    public ArticleDO getArticleById(Long articleId) {
        ArticleDO article = getById(articleId);
        if (article == null || Objects.equals(article.getDeleted(), YesOrNoEnum.YES.getCode())) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }
        return article;
    }

    public void updateArticleCopy(Long articleId, String content) {
        ArticleDetailDO detail = findLatestDetail(articleId);
        detail.setCopy(content);
        articleDetailMapper.updateById(detail);
    }
}
