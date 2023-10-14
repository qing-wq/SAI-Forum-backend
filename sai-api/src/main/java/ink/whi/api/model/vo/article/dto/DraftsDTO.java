package ink.whi.api.model.vo.article.dto;

import ink.whi.api.model.base.BaseDTO;
import lombok.Data;

import java.io.Serial;
import java.util.List;
import java.util.Set;

/**
 * @author: qing
 * @Date: 2023/10/10
 */
@Data
public class DraftsDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 9140432248863331673L;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 关联的文章
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 文章头图
     */
    private String picture;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 来源：1-转载,2-原创,3-翻译
     */
    private Integer source;

    /**
     * 原文链接
     */
    private String sourceUrl;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private Set<Long> tagIds;
}
