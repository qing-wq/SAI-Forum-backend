package ink.whi.service.user.service.user;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.FollowStateEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.article.dto.YearArticleDTO;
import ink.whi.api.model.vo.article.dto.ArticleFootCountDTO;
import ink.whi.api.model.vo.article.req.UserInfoSaveReq;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.api.model.vo.user.req.UserSaveReq;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.repo.dao.UserRelationDao;
import ink.whi.service.user.repo.entity.UserDO;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ink.whi.service.user.converter.UserConverter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    private ArticleReadService articleReadService;

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
        UserStatisticInfoDTO userHomeDTO = querySimpleUserInfoWithStatistic(userId);

        // 用户资料完整度
        int cnt = 0;
        if (StringUtils.isNotBlank(userHomeDTO.getStudentId())) {
            ++cnt;
        }
        if (StringUtils.isNotBlank(userHomeDTO.getCollege())) {
            ++cnt;
        }
        if (StringUtils.isNotBlank(userHomeDTO.getEmail())) {
            ++cnt;
        }
        if (StringUtils.isNotBlank(userHomeDTO.getMajor())) {
            ++cnt;
        }
        userHomeDTO.setInfoPercent(cnt * 100 / 4);

        // 加入天数
        Integer joinDayCount = (int) ((System.currentTimeMillis() - userHomeDTO.getCreateTime().getTime()) / (1000 * 3600 * 24));
        userHomeDTO.setJoinDayCount(Math.max(1, joinDayCount));

        // 创作历程
        List<YearArticleDTO> yearArticleDTOS = articleDao.listYearArticleByUserId(userId);
        userHomeDTO.setYearArticleList(yearArticleDTOS);
        return userHomeDTO;
    }

    @Override
    public UserStatisticInfoDTO querySimpleUserInfoWithStatistic(Long userId) {
        BaseUserInfoDTO userInfoDTO = queryBasicUserInfo(userId);
        UserStatisticInfoDTO userHomeDTO = UserConverter.toUserHomeDTO(userInfoDTO);

        // 获取文章相关统计
        ArticleFootCountDTO articleFootCountDTO = countService.queryArticleCountInfoByUserId(userId);
        if (articleFootCountDTO != null) {
            userHomeDTO.setPraiseCount(articleFootCountDTO.getPraiseCount());
            userHomeDTO.setReadCount(articleFootCountDTO.getReadCount());
            userHomeDTO.setCollectionCount(articleFootCountDTO.getCollectionCount());
        } else {
            userHomeDTO.setPraiseCount(0);
            userHomeDTO.setReadCount(0);
            userHomeDTO.setCollectionCount(0);
        }

        // 获取发布文章总数
        int articleCount = articleReadService.queryArticleCount(userId);
        userHomeDTO.setArticleCount(articleCount);

        // 粉丝数
        Integer fansCount = userRelationDao.queryUserFansCount(userId);
        userHomeDTO.setFansCount(fansCount);

        // 关注人数
        int followsCount = userRelationDao.queryUserFollowsCount(userId);
        userHomeDTO.setFollowCount(followsCount);

        // 是否关注
        Long followUserId = ReqInfoContext.getReqInfo().getUserId();
        if (followUserId != null) {
            UserRelationDO userRelationDO = userRelationDao.getUserRelationByUserId(userId, followUserId);
            userHomeDTO.setFollowed(userRelationDO != null && Objects.equals(userRelationDO.getFollowState(), FollowStateEnum.FOLLOW.getCode()));

        } else {
            userHomeDTO.setFollowed(Boolean.FALSE);
        }
        return userHomeDTO;
    }

    /**
     * 创建用户
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUser(UserSaveReq req) {
        UserDO user = UserConverter.toUserDo(req);
        UserDO record = userDao.getUserByName(user.getUserName());
        if (record != null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "用户已存在");
        }
        userDao.saveUser(user);
        UserInfoDO userInfo = UserConverter.toUserInfoDo(req);
        userInfo.setUserId(user.getId());
        userInfo.setUserName("默认用户" + UUID.randomUUID());
        userDao.save(userInfo);
        return user.getId();
    }

    @Override
    public void saveUserInfo(UserInfoSaveReq req) {
        UserInfoDO info = UserConverter.toDo(req);
        UserInfoDO record = userDao.getByUserId(req.getUserId());
        info.setId(record.getId());
        userDao.updateById(info);
    }
}
