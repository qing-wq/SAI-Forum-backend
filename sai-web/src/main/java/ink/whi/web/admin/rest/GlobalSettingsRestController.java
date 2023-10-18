package ink.whi.web.admin.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.statistics.repo.dao.DictCommonDao;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private DictCommonDao dictCommonDao;

    /**
     * 开启后台审核
     * @return
     */
    @GetMapping(path = "review")
    @Permission(role = UserRole.ADMIN)
    public ResVo<String> save(@RequestParam(name = "enable", defaultValue = "true") boolean enable) {
        // todo: 换成redis做数据字典
        dictCommonDao.setValue(DictCommonDao.REVIEW, String.valueOf(enable));
        return ResVo.ok("ok");
    }
}