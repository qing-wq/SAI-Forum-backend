package ink.whi.service.article.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.DraftsTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;


/**
 * 草稿表
 * @author qing
 * @date 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drafts")
public class DraftsDO extends BaseDO{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关联的文章id
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
    private Long categoryId;

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
     * 草稿类型，0-普通草稿，1-文章草稿
     * 普通草稿会在草稿箱进行展示，文章草稿不会
     *
     */
    private DraftsTypeEnum draftType;

    /**
     * 是否删除
     */
    private Integer deleted;
}


