package ink.whi.api.model.vo.article.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
@ToString(callSuper = true)
public class YearArticleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6173615029149365938L;

    /**
     * 年份
     */
    private String year;

    /**
     * 文章数量
     */
    private Integer articleCount;
}
