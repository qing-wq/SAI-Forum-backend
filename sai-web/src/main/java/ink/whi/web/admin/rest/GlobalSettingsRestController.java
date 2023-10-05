package ink.whi.web.admin.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.core.config.ArticleProperties;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import org.springframework.web.bind.annotation.*;

/**
 * 全局控制器
 * @author: qing
 * @Date: 2023/10/4
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "admin/api")
public class GlobalSettingsRestController {

    /**
     * 开启后台审核
     * @return
     */
    @PostMapping(path = "review")
    @Permission(role = UserRole.ADMIN)
    public ResVo<String> save(@RequestParam(name = "enable", defaultValue = "true") boolean review) {
        ArticleProperties.enable(review);
        return ResVo.ok("ok");
    }
}
