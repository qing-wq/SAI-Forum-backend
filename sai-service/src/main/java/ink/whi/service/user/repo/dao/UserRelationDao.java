package ink.whi.service.user.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.FollowStateEnum;
import ink.whi.service.user.repo.entity.UserRelationDO;
import ink.whi.service.user.repo.mapper.UserRelationMapper;
import org.springframework.stereotype.Repository;

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
}
