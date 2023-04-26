package ink.whi.service.user.service.user;

import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.repo.entity.UserDO;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.service.UserSettingService;
import ink.whi.service.user.service.help.UserPwdEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ink.whi.service.user.converter.UserConverter;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Service
public class UserSettingServiceImpl implements UserSettingService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPwdEncoder userPwdEncoder;

    @Override
    public BaseUserInfoDTO passwordLogin(String username, String password) {
        UserDO user = userDao.getUserByName(username);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, username);
        }

        // 密码加密
        if (!userPwdEncoder.match(password, user.getPassword())) {
            throw BusinessException.newInstance(StatusEnum.USER_PWD_ERROR);
        }

        return queryBasicUserInfo(user.getId());
    }

    private BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        UserInfoDO user = userDao.getByUserId(userId);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }
        return UserConverter.toDTO(user);
    }
}
