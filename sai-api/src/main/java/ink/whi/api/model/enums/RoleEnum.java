package ink.whi.api.model.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author qing
 * @date 2023/1/31
 */
@Getter
public enum RoleEnum {
    NORMAL(0, "普通用户"),
    ADMIN(1, "超级用户"),
    ;

    private int role;
    private String desc;

    RoleEnum(int role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    public static String role(Integer roleId) {
        if (Objects.equals(roleId, 1)) {
            return ADMIN.name();
        } else {
            return NORMAL.name();
        }
    }
}
