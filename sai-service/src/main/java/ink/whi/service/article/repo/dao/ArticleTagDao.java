package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.repo.entity.ArticleTagDO;
import ink.whi.service.article.repo.entity.TagDO;
import ink.whi.service.article.repo.mapper.ArticleTagMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class ArticleTagDao extends ServiceImpl<ArticleTagMapper, ArticleTagDO> {
    /**
     * 查询文章标签详情
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

    public void saveBatch(Long articleId, Set<Long> tagIds) {
        List<ArticleTagDO> list = new ArrayList<>(tagIds.size());
        tagIds.forEach(s -> {
            ArticleTagDO tag = new ArticleTagDO();
            tag.setArticleId(articleId);
            tag.setTagId(s);
            tag.setDeleted(YesOrNoEnum.NO.getCode());
            list.add(tag);
        });
        saveBatch(list);
    }

    public void updateTags(Long articleId, Set<Long> newTags) {
        List<ArticleTagDO> oldTags = listArticleTags(articleId);
        List<ArticleTagDO> delete = new ArrayList<>();
        oldTags.forEach(s -> {
            if (newTags.contains(s.getTagId())) {
                newTags.remove(s.getTagId());
            } else {
                delete.add(s);
            }
        });
        if (CollectionUtils.isEmpty(newTags)) {
            saveBatch(articleId, newTags);
        }
        if (CollectionUtils.isEmpty(delete)) {
            List<Long> ids = delete.stream().map(BaseDO::getId).toList();
            baseMapper.deleteBatchIds(ids);
        }
    }
}
