package ink.whi.api.model.vo.notify.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum NotifyTypeEnum {
    COMMENT(1, "comment"),
    REPLY(2, "reply"),
    PRAISE(3, "praise"),
    COLLECT(4, "collect"),
    FOLLOW(5, "follow"),
    SYSTEM(6, "system"),
    DELETE_COMMENT(1, "deleteComment"),
    DELETE_REPLY(2, "deleteReply"),
    CANCEL_PRAISE(3, "cancelPraise"),
    CANCEL_COLLECT(4, "cancelCollect"),
    CANCEL_FOLLOW(5, "cancelFollow"),

    // 注册、登录添加系统相关提示消息
    REGISTER(6, "register"),
    LOGIN(6, "login"),
    ;


    private int type;
    private String msg;

    private static Map<Integer, NotifyTypeEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (NotifyTypeEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    NotifyTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    // 使用map优化查询
    public static NotifyTypeEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static NotifyTypeEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
