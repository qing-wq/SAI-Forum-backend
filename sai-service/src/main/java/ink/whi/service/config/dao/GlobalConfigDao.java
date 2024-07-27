package ink.whi.service.config.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.service.config.repo.entity.GlobalConfigDO;
import ink.whi.service.config.repo.mapper.GlobalConfigMapper;
import ink.whi.service.config.repo.params.SearchGlobalConfigParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
@Repository
public class GlobalConfigDao extends ServiceImpl<GlobalConfigMapper, GlobalConfigDO> {

    public List<GlobalConfigDO> listGlobalConfig(SearchGlobalConfigParams params) {
        LambdaQueryWrapper<GlobalConfigDO> query = buildQuery(params);
        query.select(GlobalConfigDO::getId,
                GlobalConfigDO::getKey,
                GlobalConfigDO::getValue,
                GlobalConfigDO::getComment);
        return baseMapper.selectList(query);
    }

    public Long countGlobalConfig(SearchGlobalConfigParams params) {
        return baseMapper.selectCount(buildQuery(params));
    }

    public GlobalConfigDO getGlobalConfigById(Long id) {
        // 查询的时候 deleted 为 0
        LambdaQueryWrapper<GlobalConfigDO> query = Wrappers.lambdaQuery();
        query.select(GlobalConfigDO::getId, GlobalConfigDO::getKey, GlobalConfigDO::getValue, GlobalConfigDO::getComment)
                .eq(GlobalConfigDO::getId, id)
                .eq(GlobalConfigDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectOne(query);
    }

    public void delete(GlobalConfigDO globalConfigDO) {
        globalConfigDO.setDeleted(YesOrNoEnum.YES.getCode());
        baseMapper.updateById(globalConfigDO);
    }

    private LambdaQueryWrapper<GlobalConfigDO> buildQuery(SearchGlobalConfigParams params) {
        LambdaQueryWrapper<GlobalConfigDO> query = Wrappers.lambdaQuery();

        query.and(!StringUtils.isEmpty(params.getKey()),
                        k -> k.like(GlobalConfigDO::getKey, params.getKey()))
                .and(!StringUtils.isEmpty(params.getValue()),
                        v -> v.like(GlobalConfigDO::getValue, params.getValue()))
                .and(!StringUtils.isEmpty(params.getComment()),
                        c -> c.like(GlobalConfigDO::getComment, params.getComment()))
                .eq(GlobalConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(GlobalConfigDO::getUpdateTime);
        return query;
    }
}
