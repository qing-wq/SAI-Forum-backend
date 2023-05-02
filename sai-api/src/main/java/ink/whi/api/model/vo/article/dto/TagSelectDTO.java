package ink.whi.api.model.vo.article.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class TagSelectDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1578767890250377793L;
    /**
     * 类型
     */
    private String selectType;

    /**
     * 描述
     */
    private String selectDesc;

    /**
     * 是否选中
     */
    private Boolean selected;
}
