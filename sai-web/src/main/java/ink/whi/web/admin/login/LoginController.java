package ink.whi.web.admin.login;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.req.UserSaveReq;
import ink.whi.core.limit.Limit;
import ink.whi.core.limit.LimitType;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.EmailUtil;
import ink.whi.core.utils.JwtUtil;
import ink.whi.core.utils.SessionUtil;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.service.UserService;
import ink.whi.service.user.service.UserSettingService;
import ink.whi.web.admin.login.helper.LoginHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ink.whi.service.user.service.SessionService.SESSION_KEY;

/**
 * 前后台登录接口
 *
 * @author: qing
 * @Date: 2023/4/26
 */
@RestController
@RequestMapping(path = {"/admin/login"})
public class LoginController {
    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserService userService;

    /**
     * 账号密码登录
     * application/x-www-form-urlencoded
     * @param username
     * @param password
     * @param response
     * @return
     */
    @PostMapping(path = {"/", ""})
    public ResVo<BaseUserInfoDTO> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "用户名或密码不能为空");
        }
        BaseUserInfoDTO info = userSettingService.passwordLogin(username, password);
        // 签发token
        String token = JwtUtil.createToken(info.getUserId());
        if (StringUtils.isNotBlank(token)) {
            response.addCookie(SessionUtil.newCookie(SESSION_KEY, token));
            return ResVo.ok(info);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
    }

    /**
     * 登出接口
     * @param response
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "logout")
    public ResVo<String> logout(HttpServletResponse response) {
        response.addCookie(SessionUtil.delCookie(SESSION_KEY));
        return ResVo.ok("ok");
    }

    /**
     * 用户注册
     * application/json
     * @param req
     * @return
     */
    @PostMapping(path = "register")
    public ResVo<Long> register(@Validated @RequestBody UserSaveReq req, HttpServletResponse response) {
        // 邮箱验证码校验
        loginHelper.verifyEmail(req.getEmail(), req.getCode());

        Long userId = userService.saveUser(req);
        // 签发token
        String token = JwtUtil.createToken(userId);
        if (StringUtils.isBlank(token)) {
            return ResVo.fail(StatusEnum.TOKEN_NOT_EXISTS);
        }
        response.addCookie(SessionUtil.newCookie(SESSION_KEY, token));
        return ResVo.ok(userId);
    }

    /**
     * 发送验证码
     * application/x-www-form-urlencoded
     *
     * @return
     */
    @Limit(key = "email", name = "email", limitType = LimitType.IP, count = 1, period = 60)
    @PostMapping(path = "code")
    public ResVo<String> code(@RequestParam("email") String email) {
        // 检验邮箱是否合法
        if (StringUtils.isBlank(email) && !EmailUtil.checkEmail(email)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "邮箱格式不正确");
        }

        UserInfoDO info = userService.queryUserInfoByEmail(email);
        if (info != null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "邮箱已被注册");
        }
        loginHelper.subscribe(email);
        return ResVo.ok("ok");
    }

    /**
     * 获取用户登录状态
     *
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "info")
    public ResVo<BaseUserInfoDTO> info() {
        BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        return ResVo.ok(user);
    }
}
