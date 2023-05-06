package ink.whi.api.model.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 * @author: qing
 * @Date: 2023/4/25 23:29
 */
@Getter
public class BusinessException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 2904668513164732323L;
    @Getter
    private Status status;

    public BusinessException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

    public static BusinessException newInstance(StatusEnum statusEnum, Object... args) {
        return new BusinessException(statusEnum, args);
    }
}
