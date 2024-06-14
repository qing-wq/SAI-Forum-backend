package ink.whi.api.model.event;

import ink.whi.api.model.enums.ArticleEventEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @author qing
 * @date 2023/8/10
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ArticleMsgEvent<T> extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1179281113823375354L;

    private ArticleEventEnum type;

    private T content;


    public ArticleMsgEvent(Object source, ArticleEventEnum type, T content) {
        super(source);
        this.type = type;
        this.content = content;
    }
}
