package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.req.CategoryReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;

/**
 * @author: qing
 * @Date: 2023/5/9
 */
public interface CategorySettingService {
    void saveCategory(CategoryReq req);

    void deleteCategory(Integer categoryId);

    void operateCategory(Integer categoryId, Integer pushStatus);

    PageVo<CategoryDTO> getCategoryList(PageParam newPageInstance);
}
