package ink.whi.api.model.enums;

import lombok.Getter;

/**
 * 加精状态枚举
 *
 * @author qing
 * @date 2022/7/19
 */
@Getter
public enum RecommendStateEnum {

    NOT_RECOMMEND(0, "不加精"),
    RECOMMEND(1, "加精");

    RecommendStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static RecommendStateEnum formCode(Integer code) {
        for (RecommendStateEnum value : RecommendStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return RecommendStateEnum.NOT_RECOMMEND;
    }
}
