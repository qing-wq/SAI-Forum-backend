package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.mapper.ArticleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

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
}
