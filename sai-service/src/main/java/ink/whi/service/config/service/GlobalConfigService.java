package ink.whi.service.config.service;


import ink.whi.api.model.vo.config.dto.GlobalConfigDTO;
import ink.whi.api.model.vo.config.req.GlobalConfigReq;
import ink.whi.api.model.vo.config.req.SearchGlobalConfigReq;
import ink.whi.api.model.vo.page.PageVo;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
public interface GlobalConfigService {
    PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req);

    void save(GlobalConfigReq req);

    void delete(Long id);
}
