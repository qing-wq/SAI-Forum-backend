package ink.whi.web.search.vo;

import ink.whi.api.model.vo.article.dto.SimpleArticleDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/10/28
 */
@Data
public class SearchHintsVo implements Serializable {
    private static final long serialVersionUID = -2989169905031769195L;
    /**
     * 搜索的关键词
     */
    private String key;

    private List<SimpleArticleDTO> items;
}
