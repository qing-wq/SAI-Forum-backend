package ink.whi.api.model.vo.article.dto;

import ink.whi.api.model.enums.PushStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    public static final String DEFAULT_TOTAL_CATEGORY = "全部";
    public static final CategoryDTO DEFAULT_CATEGORY = new CategoryDTO(0L, "全部");
    private static final long serialVersionUID = 8272116638231812207L;

    public static CategoryDTO EMPTY = new CategoryDTO(-1L, "illegal");

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String category;

    /**
     * 分类级别
     */
    private Integer rank;

    /**
     * 0-未发布,1-已发布
     */
    private Integer status;

    /**
     * 被选择
     */
    private Boolean selected;

    public CategoryDTO(Long categoryId, String category) {
        this(categoryId, category, 0);
    }

    public CategoryDTO(Long categoryId, String category, Integer rank) {
        this.categoryId = categoryId;
        this.category = category;
        this.status = PushStatusEnum.ONLINE.getCode();
        this.rank = rank;
        this.selected = false;
    }
}
