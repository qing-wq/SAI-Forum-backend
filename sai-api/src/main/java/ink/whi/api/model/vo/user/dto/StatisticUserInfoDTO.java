package ink.whi.api.model.vo.user.dto;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/10/2
 */
@Data
public class StatisticUserInfoDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 专业
     */
    private String major;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色 0-普通用户 1-管理员
     */
    private String role;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;
}
