package ink.whi.service.article.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * @author: qing
 * @Date: 2023/7/7
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("draft")
public class DraftDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = -7520165504247330524L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 作者ID
     */
    private Long author;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 删除
     */
    private Integer deleted;
}
