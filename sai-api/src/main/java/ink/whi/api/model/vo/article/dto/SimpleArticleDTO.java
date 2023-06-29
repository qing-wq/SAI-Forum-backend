package ink.whi.api.model.vo.article.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
@Accessors(chain = true)
public class SimpleArticleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3646376715620165839L;

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
