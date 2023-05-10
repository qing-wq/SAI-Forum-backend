package ink.whi.api.model.vo.article.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 保存Tag请求参数
 *
 * @author LouZai
 * @date 2022/9/17
 */
@Data
public class TagReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -5388767717047688998L;
    /**
     * ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tag;

    /**
     * 类目ID
     */
    private Long categoryId;
}
