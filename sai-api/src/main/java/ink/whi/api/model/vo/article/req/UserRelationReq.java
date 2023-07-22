package ink.whi.api.model.vo.article.req;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/7/22
 */
@Data
public class UserRelationReq {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followUserId;

    /**
     * 是否关注当前用户
     */
    private Boolean followed;

}
