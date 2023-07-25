package ink.whi.service.user.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.FollowStateEnum;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import ink.whi.service.user.repo.mapper.UserRelationMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class UserRelationDao extends ServiceImpl<UserRelationMapper, UserRelationDO> {

    public int queryUserFansCount(Long userId) {
        return lambdaQuery().eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowState, FollowStateEnum.FOLLOW.getCode())
                .count().intValue();
    }

    public UserRelationDO getUserRelationByUserId(Long userId, Long followUserId) {
        return lambdaQuery().eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowUserId, followUserId)
                .one();
    }

    /**
     * 查询用户的关注列表
     *
     * @param followUserId
     * @param pageParam
     * @return
     */
    public List<FollowUserInfoDTO> listUserFollows(Long followUserId, PageParam pageParam) {
        return baseMapper.queryUserFollowList(followUserId, pageParam);
    }

    /**
     * 查询用户的粉丝列表，即关注userId的用户
     *
     * @param userId
     * @param pageParam
     * @return
     */
    public List<FollowUserInfoDTO> listUserFans(Long userId, PageParam pageParam) {
        return baseMapper.queryUserFansList(userId, pageParam);
    }

    /**
     * 查询用户是否关注列表中的用户
     *
     * @param followUserId 粉丝用户id
     * @param targetUserId 关注者用户id列表
     * @return
     */
    public List<UserRelationDO> listUserRelations(Long followUserId, Collection<Long> targetUserId) {
        return lambdaQuery().eq(UserRelationDO::getFollowUserId, followUserId)
                .in(UserRelationDO::getUserId, targetUserId).list();
    }

    public UserRelationDO getUserRelationRecord(Long userId, Long followUserId) {
        return lambdaQuery().eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowUserId, followUserId)
                .one();
    }

    public int queryUserFollowsCount(Long userId) {
        return lambdaQuery().eq(UserRelationDO::getFollowUserId, userId)
                .eq(UserRelationDO::getFollowUserId, FollowStateEnum.FOLLOW.getCode())
                .count().intValue();
    }
}
