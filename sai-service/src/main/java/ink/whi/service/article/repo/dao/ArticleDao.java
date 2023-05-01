package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beust.ah.A;
import com.google.common.collect.Maps;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.YearArticleDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.help.ArticleHelper;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import ink.whi.service.article.repo.entity.ReadCountDO;
import ink.whi.service.article.repo.mapper.ArticleDetailMapper;
import ink.whi.service.article.repo.mapper.ArticleMapper;
import ink.whi.service.article.repo.mapper.ReadCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

    @Resource
    private ArticleDetailMapper articleDetailMapper;

    @Resource
    private ReadCountMapper readCountMapper;

    @Autowired
    private ArticleHelper articleHelper;

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
        if (articleHelper.showReviewContent(article)) {
            ArticleDetailDO detail = articleHelper.findLatestDetail(articleId);
            dto.setContent(detail.getContent());
        } else {
            // 对于审核中的文章，只有作者本人才能看到原文
            dto.setContent("### 文章审核中，请稍后再看");
        }
        return dto;
    }

    /**
     * 查询分类对应文章内容
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
     * 若文章处于审核状态，则直接更新上一条记录；否则新插入一条记录
     * 即对于审核之前的内容不保存历史版本，上线后的文章保存历史版本
     * @param articleId
     * @param content
     * @param review
     */
    public void updateArticleContent(Long articleId, String content, boolean review) {
        if (review) {
            articleDetailMapper.updateContent(articleId, content);
        } else {
            ArticleDetailDO detail = articleHelper.findLatestDetail(articleId);
            detail.setVersion(detail.getVersion() + 1);
            detail.setContent(content);
            articleDetailMapper.insert(detail);
        }
    }
}
