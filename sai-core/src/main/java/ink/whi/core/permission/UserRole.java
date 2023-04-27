package ink.whi.core.permission;

import lombok.Getter;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Getter
public enum UserRole {
    /**
     * 管理员
     */
    ADMIN,
    /**
     * 登录
     */
    LOGIN,
    /**
     * 全部用户
     */
    ALL
}
