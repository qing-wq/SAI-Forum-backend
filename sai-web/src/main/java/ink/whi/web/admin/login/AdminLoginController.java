package ink.whi.web.admin.login;

import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.utils.JwtUtil;
import ink.whi.service.user.service.SessionService;
import ink.whi.service.user.service.UserSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前后台登录接口
 *
 * @author: qing
 * @Date: 2023/4/26
 */
@RestController
@RequestMapping(path = {"/admin/login"})
public class AdminLoginController{
    @Autowired
    private UserSettingService userSettingService;

    /**
     * 账号密码登录
     * @param request
     * @param response
     * @return
     */
    @PostMapping(path = {"/", ""})
    public ResVo<BaseUserInfoDTO> login(HttpServletRequest request,
                                        HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "用户名或密码不能为空");
        }
        BaseUserInfoDTO info = userSettingService.passwordLogin(username, password);
        // 签发token
        String token = JwtUtil.createToken(info.getUserId());
        if (StringUtils.isNotBlank(token)) {
            response.addCookie(new Cookie(SessionService.SESSION_KEY, token));
            return ResVo.ok(info);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
    }
}
