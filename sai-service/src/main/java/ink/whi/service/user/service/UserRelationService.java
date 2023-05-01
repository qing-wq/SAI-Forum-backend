package ink.whi.service.user.service;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;

/**
 * @author: qing
 * @Date: 2023/5/1
 */
public interface UserRelationService {

    PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam);

    PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam);

    void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long loginUserId);
}
