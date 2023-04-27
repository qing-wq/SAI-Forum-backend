package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.mapper.ArticleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

    public List<ArticleDTO> listArticleByCategoryId(Long categoryId, PageParam pageParam) {
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
        List<ArticleDO> list = baseMapper.selectList(query);
        return ArticleConverter.toArticleDtoList(list);
    }
}
