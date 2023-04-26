package ink.whi.api.model.exception;

import ink.whi.api.model.enums.StatusEnum;

/**
 * @author: qing
 * @Date: 2023/4/26 10:42
 */
public class ExceptionUtils {
    public static ForumException of(StatusEnum statusEnum, Object... arg) {
        return new ForumException(statusEnum, arg);
    }
}
