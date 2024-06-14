package ink.whi.api.model.event;

import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @author qing
 * @date 2022/4/30
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NotifyMsgEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 3769329609100817957L;

    private NotifyTypeEnum notifyType;

    private T content;


    public NotifyMsgEvent(Object source, NotifyTypeEnum notifyType, T content) {
        super(source);
        this.notifyType = notifyType;
        this.content = content;
    }
}
