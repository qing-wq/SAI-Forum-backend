package ink.whi.web.Index.vo;

import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class IndexVo {

    /**
     * 分类列表
     */
    List<CategoryDTO> categories;

    /**
     * 当前选中的分类
     */
    private String currentCategory;

    /**
     * 当前选中的类目id
     */
    private Long categoryId;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> articles;

    /**
     * 登录用户信息
     */
//    private UserStatisticInfoDTO user;
    private BaseUserInfoDTO user;
}
