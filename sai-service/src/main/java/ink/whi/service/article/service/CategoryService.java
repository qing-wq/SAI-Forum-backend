package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.dto.CategoryDTO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface CategoryService {
    String queryCategoryName(Long categoryId);

    List<CategoryDTO> loadAllCategories();

    void refreshCache();

    Long queryCategoryId(String category);
}
