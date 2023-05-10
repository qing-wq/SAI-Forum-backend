package ink.whi.api.model.vo.article.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Category请求参数
 *
 * @author qing
 * @date 2023/5/10
 */
@Data
public class CategoryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -2853821147074605115L;
    /**
     * ID
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String category;

    /**
     * 排序
     */
    private Integer rank;
}
