package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.entity.TagDO;
import ink.whi.service.article.repo.mapper.TagMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
@Repository
public class TagDao extends ServiceImpl<TagMapper, TagDO> {

    public List<TagDTO> listTags(PageParam pageParam) {
        List<TagDO> list = lambdaQuery().last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        return ArticleConverter.toDtoList(list);
    }

    public List<TagDTO> listTag(PageParam pageParam) {
        List<TagDO> list = lambdaQuery().eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return ArticleConverter.toDtoList(list);
    }

    public Integer countTag() {
        return lambdaQuery().eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }
}
