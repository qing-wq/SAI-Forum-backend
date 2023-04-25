package ink.whi.api.model.exception;

import ink.whi.api.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: qing
 * @Date: 2023/4/25 23:34
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private int code;

    private String msg;

    public static Status newStatus(int code, String msg) {
        return new Status(code, msg);
    }

    public static Status newStatus(StatusEnum statusEnum, Object... messages) {
        String msg;
        if (messages.length > 0) {
            msg = String.format(statusEnum.getMsg(), messages);
        } else {
            msg = statusEnum.getMsg();
        }
        return newStatus(statusEnum.getCode(), msg);
    }
}
