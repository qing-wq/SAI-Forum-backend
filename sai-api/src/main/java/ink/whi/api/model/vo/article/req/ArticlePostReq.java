package ink.whi.api.model.vo.article.req;

import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.SourceTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 发布文章入参
 *
 * @author qing
 * @date 2024/8/30
 */
@Data
public class ArticlePostReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 3360181337899172705L;
    /**
     * 文章ID， 当存在时，表示更新文章
     */
    private Long articleId;

    /**
     * 草稿id
     */
    private Long draftId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章短标题
     */
    private String shortTitle;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 标签
     */
    private Set<Long> tagIds;

    /**
     * 简介
     */
    private String summary;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 封面
     */
    private String picture;

    /**
     * 文章类型 0-帖子, 1-博客, 2-帖子
     *
     * @see ArticleTypeEnum
     */
    private String articleType;


    /**
     * 来源：1-转载，2-原创，3-翻译
     *
     * @see SourceTypeEnum
     */
    private Integer source;

    /**
     * 状态：0-未发布，1-已发布
     *
     * @see PushStatusEnum
     */
    private Integer status;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * POST 发表, SAVE 暂存
     */
    private String actionType;

    public PushStatusEnum pushStatus() {
        if ("post".equalsIgnoreCase(actionType)) {
            return PushStatusEnum.ONLINE;
        } else {
            return PushStatusEnum.OFFLINE;
        }
    }

    public boolean deleted() {
        return "delete".equalsIgnoreCase(actionType);
    }
}