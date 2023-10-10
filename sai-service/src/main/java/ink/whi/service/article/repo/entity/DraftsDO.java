package ink.whi.service.article.repo.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


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
     * 关联的文章,0-没有
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
     * 是否删除
     */
    private Integer deleted;
}


