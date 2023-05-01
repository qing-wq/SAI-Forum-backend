package ink.whi.service.user.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;
import ink.whi.service.user.repo.entity.UserRelationDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public interface UserRelationMapper extends BaseMapper<UserRelationDO> {
    List<FollowUserInfoDTO> queryUserFansList(Long userId, PageParam pageParam);

    List<FollowUserInfoDTO> queryUserFollowList(Long followUserId, PageParam pageParam);
}
