package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.entity.TagDO;
import ink.whi.service.article.repo.mapper.TagMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
@Repository
public class TagDao extends ServiceImpl<TagMapper, TagDO> {

    public List<TagDTO> listTags(PageParam pageParam) {
        List<TagDO> list = lambdaQuery()
                .eq(TagDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        return ArticleConverter.toDtoList(list);
    }

    /**
     * 根据标签关键字查询标签ids
     * @param key
     * @return
     */
    public List<Long> listTagsByKey(String key) {
        if (key.isBlank()) {
            return Collections.emptyList();
        }
        List<TagDO> tagList = lambdaQuery().like(TagDO::getTagName, key).list();
        return tagList.stream().map(TagDO::getId).toList();
    }

    public Integer countTag() {
        return lambdaQuery().eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }
}
