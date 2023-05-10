package ink.whi.service.article.service.Impl;

import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.req.CategoryReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.CategoryDao;
import ink.whi.service.article.repo.entity.CategoryDO;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.article.service.CategorySettingService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/9
 */
@Service
public class CategorySettingServiceImpl implements CategorySettingService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveCategory(CategoryReq req) {
        CategoryDO categoryDO = ArticleConverter.toDO(req);
        if (NumUtil.upZero(req.getCategoryId())) {
            categoryDao.save(categoryDO);
        } else {
            categoryDao.updateById(categoryDO);
        }
        categoryService.refreshCache();  // 刷新缓存
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        CategoryDO record = categoryDao.getById(categoryId);
        if (record == null) {
            return;
        }
        record.setDeleted(YesOrNoEnum.YES.getCode());
        categoryDao.updateById(record);
        categoryService.refreshCache();
    }

    @Override
    public void operateCategory(Integer categoryId, Integer pushStatus) {
        CategoryDO record = categoryDao.getById(categoryId);
        if (record == null) {
            return;
        }
        record.setStatus(pushStatus);
        categoryDao.updateById(record);
        categoryService.refreshCache();
    }

    @Override
    public PageVo<CategoryDTO> getCategoryList(PageParam pageParam) {
        List<CategoryDTO> list = categoryDao.listCategory(pageParam);
        Integer totalCount = categoryDao.countCategory();
        return PageVo.build(list, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}
