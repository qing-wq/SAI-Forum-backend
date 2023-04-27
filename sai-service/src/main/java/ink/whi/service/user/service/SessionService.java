package ink.whi.service.user.service;

import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public interface SessionService {
    void updateUserIpInfo(BaseUserInfoDTO user, String clientIp);
}
