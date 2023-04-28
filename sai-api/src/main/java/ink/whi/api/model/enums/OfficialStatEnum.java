package ink.whi.api.model.enums;

import lombok.Getter;

/**
 * 官方状态枚举， 与DB一致
 *
 * @author qing
 * @date 2022/7/19
 */
@Getter
public enum OfficialStatEnum {

    NOT_OFFICIAL(0, "非官方"),
    OFFICIAL(1, "官方");

    OfficialStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OfficialStatEnum formCode(Integer code) {
        for (OfficialStatEnum value : OfficialStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OfficialStatEnum.NOT_OFFICIAL;
    }
}
