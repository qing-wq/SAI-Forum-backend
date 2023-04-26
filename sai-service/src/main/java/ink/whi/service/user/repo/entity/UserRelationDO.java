package ink.whi.service.user.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户关系表
 *
 * @author qing
 * @date 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_relation")
public class UserRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主用户ID
     */
    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followUserId;

    /**
     * 关注状态: 0-未关注，1-已关注，2-取消关注
     */
    private Integer followState;
}
