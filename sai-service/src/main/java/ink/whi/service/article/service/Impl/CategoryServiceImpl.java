package ink.whi.service.article.service.Impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.CategoryDao;
import ink.whi.service.article.repo.entity.CategoryDO;
import ink.whi.service.article.service.CategoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    // fixme: 本地缓存，如果改为分布式会有问题
    private LoadingCache<Long, CategoryDTO> categoryCaches;

    private CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @PostConstruct
    public void init() {
        categoryCaches = CacheBuilder.newBuilder().maximumSize(300).build(new CacheLoader<Long, CategoryDTO>() {
            @Override
            public CategoryDTO load(@NotNull Long categoryId) throws Exception {
                CategoryDO category = categoryDao.getById(categoryId);
                if (category == null || category.getDeleted() == YesOrNoEnum.YES.getCode()) {
                    return CategoryDTO.EMPTY;
                }
                return new CategoryDTO(categoryId, category.getCategoryName(), category.getRank());
            }
        });
        refreshCache();  // 将数据放入缓存
    }

    /**
     * 查询类目名
     *
     * @param categoryId
     * @return
     */
    @Override
    public String queryCategoryName(Long categoryId) {
        return categoryCaches.getUnchecked(categoryId).getCategory();
    }

    /**
     * 查询所有的分类
     *
     * @return
     */
    @Override
    public List<CategoryDTO> loadAllCategories() {
        if (categoryCaches.size() <= 5) {
            refreshCache();
        }
        List<CategoryDTO> list = new ArrayList<>(categoryCaches.asMap().values());
        list.removeIf(s -> s.getCategoryId() <= 0);
        list.sort(Comparator.comparingLong(CategoryDTO::getRank));
        return list;
    }

    /**
     * 刷新缓存
     */
    @Override
    public void refreshCache() {
        List<CategoryDO> list = categoryDao.listAllCategoriesFromDb();
        categoryCaches.invalidateAll();
        categoryCaches.cleanUp();
        list.forEach(s -> categoryCaches.put(s.getId(), ArticleConverter.toDto(s)));
    }

    @Override
    public Long queryCategoryId(String category) {
        return categoryDao.getIdByCategoryName(category);
    }
}
