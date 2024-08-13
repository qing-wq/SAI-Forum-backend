package ink.whi.api.model.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * 配置变更消息事件
 *
 * @author qing
 * @date 2024/8/10
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ConfigRefreshEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 3876608871978439033L;

    private String key;
    private String val;

    public ConfigRefreshEvent(Object source, String key, String value) {
        super(source);
        this.key = key;
        this.val = value;
    }
}
