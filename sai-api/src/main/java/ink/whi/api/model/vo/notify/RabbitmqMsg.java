package ink.whi.api.model.vo.notify;

import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/8/2
 */
@Getter
@Setter
@ToString
public class RabbitmqMsg<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3769329609100817957L;

    private NotifyTypeEnum notifyType;

    private T content;

    public RabbitmqMsg() {
    }

    public RabbitmqMsg(NotifyTypeEnum notifyType, T content) {
        this.notifyType = notifyType;
        this.content = content;
    }
}
