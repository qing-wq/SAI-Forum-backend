package ink.whi.api.model.vo.article.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class TagDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8614833588325787479L;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tag;

    /**
     * 0-未发布,1-已发布
     */
    private Integer status;

    /**
     * 1-被选择
     */
    private Boolean selected;
}
