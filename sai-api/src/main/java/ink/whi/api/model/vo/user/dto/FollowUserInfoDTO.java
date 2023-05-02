package ink.whi.api.model.vo.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 关注者用户信息
 *
 * @author qing
 * @date 2023/5/1
 */
@Data
public class FollowUserInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7765991272483829099L;
    /**
     * 当前登录的用户与这个用户之间的关联关系id
     */
    private Long relationId;

    /**
     * true 表示当前登录用户关注了这个用户
     * false 标识当前登录用户没有关注这个用户
     */
    private Boolean followed;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;
}
