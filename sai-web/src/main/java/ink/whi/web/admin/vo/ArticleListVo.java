package ink.whi.web.admin.vo;

import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.page.PageVo;
import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/10/5
 */
@Data
public class ArticleListVo {
    /**
     * 文章信息
     */
    public PageVo<ArticleDTO> page;
    /**
     * 是否开启审核
     */
    public Boolean review;
}
