package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.service.article.repo.entity.CategoryDO;
import ink.whi.service.article.repo.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class CategoryDao extends ServiceImpl<CategoryMapper, CategoryDO> {
    /**
     * 获取分类ID
     * @param category
     * @return
     */
    public Long getIdByCategoryName(String category) {
        return lambdaQuery().eq(CategoryDO::getCategoryName, category)
                .eq(CategoryDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one().getId();
    }

    public List<CategoryDO> listAllCategoriesFromDb() {
        return lambdaQuery().eq(CategoryDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(CategoryDO::getRank)
                .list();
    }
}
