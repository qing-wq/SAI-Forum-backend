package ink.whi.api.model.enums;

import lombok.Getter;

/**
 * 文章类型枚举
 */
@Getter
public enum ArticleTypeEnum {

    DRAFT(0, "草稿"),
    BLOG(1, "博文"),
    ANSWER(2, "帖子");

    ArticleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleTypeEnum formCode(Integer code) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
