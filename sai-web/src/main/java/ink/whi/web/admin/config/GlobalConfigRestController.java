package ink.whi.web.admin.config;

import ink.whi.api.model.event.ConfigRefreshEvent;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.config.dto.GlobalConfigDTO;
import ink.whi.api.model.vo.config.req.GlobalConfigReq;
import ink.whi.api.model.vo.config.req.SearchGlobalConfigReq;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.config.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

/**
 * 全局配置
 *
 * @author: qing
 * @Date: 2024/8/29
 */
@RestController
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = {"admin/global/config/"})
public class GlobalConfigRestController {

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 配置保存/修改
     * @param req
     * @return
     */
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody GlobalConfigReq req) {
        globalConfigService.save(req);
        return ResVo.ok();
    }

    /**
     * 配置删除
     * @param id
     * @return
     */
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "id") Long id) {
        globalConfigService.delete(id);
        return ResVo.ok();
    }

    /**
     * 配置搜索
     * @param req
     * @return
     */
    @PostMapping(path = "list")
    public ResVo<PageVo<GlobalConfigDTO>> list(@RequestBody SearchGlobalConfigReq req) {
        PageVo<GlobalConfigDTO> page = globalConfigService.getList(req);
        return ResVo.ok(page);
    }

    @EventListener
    public void handleConfigRefreshEvent(ConfigRefreshEvent event) {

    }
}
