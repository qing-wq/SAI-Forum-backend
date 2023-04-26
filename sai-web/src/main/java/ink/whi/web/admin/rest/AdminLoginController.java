package ink.whi.web.admin.rest;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import ink.whi.api.model.base.BaseDTO;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.user.service.UserSettingService;
import ink.whi.web.base.BaseRestController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@RestController
@RequestMapping(path = {"/admin/login"})
public class AdminLoginController extends BaseRestController {
    @Autowired
    private UserSettingService userSettingService;

    @PostMapping(path = {"/", ""})
    public ResVo<BaseUserInfoDTO> login(HttpServletRequest request,
                                        HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "用户名或密码不能为空");
        }
        BaseUserInfoDTO info = userSettingService.passwordLogin(username, password);
        // 将用户信息存入session
        return ResVo.ok(info);
    }
}
