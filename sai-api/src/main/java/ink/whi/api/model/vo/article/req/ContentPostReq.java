package ink.whi.api.model.vo.article.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/7/4
 */
@Data
public class ContentPostReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 3964314951433859130L;

    /**
     * 文章内容
     */
    public String content;
}
