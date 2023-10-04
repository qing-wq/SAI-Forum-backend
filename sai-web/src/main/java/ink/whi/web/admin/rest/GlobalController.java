package ink.whi.web.admin.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.core.config.ArticleProperties;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 全局控制器
 * @author: qing
 * @Date: 2023/10/4
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "admin/")
public class GlobalController {

    @Autowired
    private ArticleProperties articleProperties;

    /**
     * 开启后台审核
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "/enable/review/{review}")
    public ResVo<String> save(@PathVariable boolean review) {
        articleProperties.setReview(review);
        return ResVo.ok("ok");
    }
}
