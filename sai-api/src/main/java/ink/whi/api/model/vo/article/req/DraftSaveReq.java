package ink.whi.api.model.vo.article.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 草稿保存入参
 * @author: qing
 * @Date: 2023/7/6
 */
@Data
public class DraftSaveReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -4091390798510714255L;

    /**
     * 文章ID(可选)
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;
}
