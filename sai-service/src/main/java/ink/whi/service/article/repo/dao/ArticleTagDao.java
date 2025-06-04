package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.repo.entity.ArticleTagDO;
import ink.whi.service.article.repo.mapper.ArticleTagMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class ArticleTagDao extends ServiceImpl<ArticleTagMapper, ArticleTagDO> {

    @Autowired
    private TagDao tagDao;

    /**
     * 查询文章标签详情
     *
     * @param articleId
     * @return
     */
    public List<TagDTO> listArticleTagsDetail(Long articleId) {
        return baseMapper.listArticleTagDetails(articleId);
    }

    public List<ArticleTagDO> listArticleTags(Long articleId) {
        return lambdaQuery().eq(ArticleTagDO::getArticleId, articleId)
                .eq(ArticleTagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .list();
    }

    public void insertBatch(Long articleId, Set<Long> tagIds, ArticleTypeEnum type) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return;
        }

        List<ArticleTagDO> list = new ArrayList<>(tagIds.size());
        tagIds.forEach(s -> {
            ArticleTagDO tag = new ArticleTagDO();
            tag.setArticleId(articleId);
            tag.setTagId(s);
            tag.setDeleted(YesOrNoEnum.NO.getCode());
            tag.setArticleType(type.getCode());
            list.add(tag);
        });
        saveBatch(list);
    }

    /**
     * 通过标签搜索文章
     * @param keyword
     * @return
     */
    public List<Long> searchArticleByTags(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Collections.emptyList();
        }

        List<Long> tagIds = tagDao.listTagsByKey(keyword);
        if (tagIds.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryChainWrapper<ArticleTagDO> chainWrapper = ChainWrappers.lambdaQueryChain(baseMapper);
        List<ArticleTagDO> articleTags = chainWrapper.in(ArticleTagDO::getTagId, tagIds).list();
        if (CollectionUtils.isEmpty(articleTags)) {
            return Collections.emptyList();
        }
        return articleTags.stream().map(ArticleTagDO::getArticleId).collect(Collectors.toList());
    }

    /**
     * 使用新标签替换旧标签
     *
     * @param articleId
     * @param newTags
     * @param type
     */
    public void updateTags(Long articleId, Set<Long> newTags, ArticleTypeEnum type) {
        if (newTags == null) {
            return;
        }

        List<ArticleTagDO> oldTags = listArticleTags(articleId);
        List<ArticleTagDO> delete = new ArrayList<>();
        oldTags.forEach(s -> {
            if (newTags.contains(s.getTagId())) {
                newTags.remove(s.getTagId());
            } else {
                delete.add(s);
            }
        });

        insertBatch(articleId, newTags, type);
        if (!CollectionUtils.isEmpty(delete)) {
            List<Long> ids = delete.stream().map(BaseDO::getId).toList();
            baseMapper.deleteBatchIds(ids);
        }
    }

    public void deleteArticleTags(Long articleId) {
        lambdaUpdate().eq(ArticleTagDO::getArticleId, articleId)
                .eq(ArticleTagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .set(ArticleTagDO::getDeleted, YesOrNoEnum.YES.getCode())
                .update();
    }

    public Set<Long> listArticleTagIds(Long articleId) {
        return listArticleTags(articleId).stream().map(ArticleTagDO::getTagId)
                .collect(Collectors.toSet());
    }
}
