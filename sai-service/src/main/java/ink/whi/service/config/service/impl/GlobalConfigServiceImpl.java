package ink.whi.service.config.service.impl;


import ink.whi.api.model.event.ConfigRefreshEvent;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.config.dto.GlobalConfigDTO;
import ink.whi.api.model.vo.config.req.GlobalConfigReq;
import ink.whi.api.model.vo.config.req.SearchGlobalConfigReq;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.utils.NumUtil;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.config.converter.ConfigStructMapper;
import ink.whi.service.config.dao.GlobalConfigDao;
import ink.whi.service.config.repo.entity.GlobalConfigDO;
import ink.whi.service.config.repo.params.SearchGlobalConfigParams;
import ink.whi.service.config.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
@Service
public class GlobalConfigServiceImpl implements GlobalConfigService {

    @Autowired
    private GlobalConfigDao configDao;

    @Override
    public PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req) {
        ConfigStructMapper mapper = ConfigStructMapper.INSTANCE;
        SearchGlobalConfigParams params = mapper.toSearchGlobalParams(req);
        List<GlobalConfigDO> list = configDao.listGlobalConfig(params);
        Long total = configDao.countGlobalConfig(params);
        return PageVo.build(mapper.toGlobalDTOS(list), params.getPageSize(), params.getPageNum(), total);
    }

    @Override
    public void save(GlobalConfigReq req) {
        GlobalConfigDO globalConfigDO = ConfigStructMapper.INSTANCE.toGlobalDO(req);
        // id 不为空
        if (NumUtil.nullOrZero(globalConfigDO.getId())) {
            configDao.save(globalConfigDO);
        } else {
            configDao.updateById(globalConfigDO);
        }

        // 配置更新之后，主动触发配置的动态加载
        SpringUtil.publishEvent(new ConfigRefreshEvent(this, req.getKeywords(), req.getValue()));
    }

    @Override
    public void delete(Long id) {
        GlobalConfigDO globalConfigDO = configDao.getGlobalConfigById(id);
        if (globalConfigDO != null) {
            configDao.delete(globalConfigDO);
        } else {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, "记录不存在");
        }
    }
}
