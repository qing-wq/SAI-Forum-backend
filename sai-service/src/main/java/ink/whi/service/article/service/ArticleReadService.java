package ink.whi.service.article.service;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;

/**
 * 文章读接口
 *
 * @author: qing
 * @Date: 2023/4/27
 */
public interface ArticleReadService {
    PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam newPageInstance);
}
