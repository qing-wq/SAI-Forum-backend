package ink.whi.service.user.service;

import ink.whi.api.model.vo.article.req.UserInfoSaveReq;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.api.model.vo.user.req.UserSaveReq;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public interface UserService {
    BaseUserInfoDTO queryBasicUserInfo(Long userId);

    UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId);

    UserStatisticInfoDTO querySimpleUserInfoWithStatistic(Long userId);

    Long saveUser(UserSaveReq req);

    void saveUserInfo(UserInfoSaveReq req);

    void updateUserPwd(Long userId, String olderPassword, String newPassword);
}
