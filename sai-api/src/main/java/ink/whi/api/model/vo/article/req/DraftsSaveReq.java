package ink.whi.api.model.vo.article.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 草稿保存入参
 * @author: qing
 * @Date: 2023/7/6
 */
@Data
public class DraftsSaveReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -4091390798510714255L;

    /**
     * 草稿id，必选
     */
    private Long draftId;

    /**
     * 关联的文章, 0-没有（非必填）
     */
    private Long articleId;

    /**
     * 用户id（非必须）
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 标签
     */
    private Set<Long> tagIds;

    /**
     * 文章内容
     */
    private String content;

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
}
