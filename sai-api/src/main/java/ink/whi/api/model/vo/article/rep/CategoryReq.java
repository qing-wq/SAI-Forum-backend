package ink.whi.api.model.vo.article.rep;

import lombok.Data;

import java.io.Serializable;

/**
 * 保存Category请求参数
 *
 * @author LouZai
 * @date 2022/9/17
 */
@Data
public class CategoryReq implements Serializable {

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
