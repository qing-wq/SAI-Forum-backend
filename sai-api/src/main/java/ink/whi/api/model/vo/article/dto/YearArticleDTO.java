package ink.whi.api.model.vo.article.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
@ToString(callSuper = true)
public class YearArticleDTO {

    /**
     * 年份
     */
    private String year;

    /**
     * 文章数量
     */
    private Integer articleCount;
}
