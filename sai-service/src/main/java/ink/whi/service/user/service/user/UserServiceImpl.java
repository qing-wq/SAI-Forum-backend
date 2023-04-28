package ink.whi.service.user.service.user;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.article.dto.YearArticleDTO;
import ink.whi.api.model.vo.user.dto.ArticleFootCountDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.repo.dao.UserRelationDao;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserService;
import ink.whi.service.user.service.count.CountServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ink.whi.service.user.converter.UserConverter;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRelationDao userRelationDao;

    @Autowired
    private CountService countService;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        UserInfoDO user = userDao.getByUserId(userId);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }
        return UserConverter.toDTO(user);
    }

    @Override
    public UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId) {
        return null;
    }
}
