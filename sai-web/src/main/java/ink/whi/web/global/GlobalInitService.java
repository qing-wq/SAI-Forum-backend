package ink.whi.web.global;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.utils.JwtUtil;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.service.NotifyMsgService;
import ink.whi.service.user.service.SessionService;
import ink.whi.service.user.service.UserSettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Slf4j
@Service
public class GlobalInitService {

    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private NotifyMsgService notifyService;

    @Autowired
    private SessionService sessionService;

    public void initLoginUser(ReqInfoContext.ReqInfo reqInfo) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        BaseUserInfoDTO user = VerifyToken(request, response);
        if (user != null) {
            reqInfo.setUserId(user.getUserId());
            reqInfo.setUser(user);
            reqInfo.setMsgNum(notifyService.queryUserNotifyMsgCount(user.getUserId()));
            // 更新ip信息
            sessionService.updateUserIpInfo(user, reqInfo.getClientIp());
        }
    }

    /**
     * 校验token
     *
     * @param req
     * @param res
     */
    private BaseUserInfoDTO VerifyToken(HttpServletRequest req, HttpServletResponse res) {
        String token = req.getHeader(JwtUtil.Authorization);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Long userId = JwtUtil.isVerify(token);
        BaseUserInfoDTO user = userSettingService.queryBasicUserInfo(userId);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.JWT_VERIFY_EXISTS);
        }

        // 检查token是否需要更新
        if (JwtUtil.isNeedUpdate(token)) {
            token = JwtUtil.createToken(userId);
            res.setHeader(JwtUtil.Authorization, token);
        }
        return user;
    }
}
