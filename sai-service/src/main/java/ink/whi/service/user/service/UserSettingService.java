package ink.whi.service.user.service;

import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.StatisticUserInfoDTO;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public interface UserSettingService {
    BaseUserInfoDTO passwordLogin(String username, String password);

    BaseUserInfoDTO queryBasicUserInfo(Long userId);

    Integer getUserCount();

    PageVo<StatisticUserInfoDTO> getUserList(PageParam pageParam);

    void updateIpInfo(Long userId, String clientIp);
}
