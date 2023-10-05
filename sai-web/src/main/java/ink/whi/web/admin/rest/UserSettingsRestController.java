package ink.whi.web.admin.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.api.model.vo.user.dto.StatisticUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.user.service.UserSettingService;
import ink.whi.web.base.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理后台
 * @author: qing
 * @Date: 2023/10/2
 */
@RestController
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = "/admin/user")
public class UserSettingsRestController extends BaseRestController {
    @Autowired
    private UserSettingService userSettingService;

    /**
     * 获取用户列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/list")
    public ResVo<PageVo<StatisticUserInfoDTO>> getUserList(@RequestParam(name = "page", required = false) Long pageNumber,
                                                           @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNumber, pageSize);
        PageVo<StatisticUserInfoDTO> userList = userSettingService.getUserList(pageParam);
        return ResVo.ok(userList);
    }
}
