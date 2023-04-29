package ink.whi.api.model.enums;

import lombok.Getter;

@Getter
public enum CommentStatEnum {

    NOT_COMMENT(0, "未评论"),
    COMMENT(1, "已评论"),
    DELETE_COMMENT(2, "删除评论");

    CommentStatEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static CommentStatEnum formCode(Integer code) {
        for (CommentStatEnum statEnum : CommentStatEnum.values()) {
            if (statEnum.getCode().equals(code)) {
                return statEnum;
            }
        }
        return CommentStatEnum.NOT_COMMENT;
    }
}
