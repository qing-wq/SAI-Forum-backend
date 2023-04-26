package ink.whi.api.model.exception;

import ink.whi.api.model.enums.StatusEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 * @author: qing
 * @Date: 2023/4/25 23:29
 */
@Getter
public class ForumException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 2904668513164732323L;
    @Getter
    private Status status;

    public ForumException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

    public static ForumException newInstance(StatusEnum statusEnum, Object... args) {
        return new ForumException(statusEnum, args);
    }
}
