package ink.whi.web.Index.helper;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.Index.vo.IndexVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 首页相关
 *
 * @author qing
 * @date 2023/4/27
 */
@Component
public class IndexRecommendHelper {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private UserService userService;

    public IndexVo buildIndexVo(String activeTab) {
        IndexVo vo = new IndexVo();
        CategoryDTO category = categories(activeTab, vo);
        vo.setArticles(articleList(category.getCategoryId()));
        vo.setCurrentCategory(category.getCategory());
        vo.setCategoryId(category.getCategoryId());
        vo.setUser(userInfo());
        return vo;
    }

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> articleList(Long categoryId) {
        return articleReadService.queryArticlesByCategory(categoryId, PageParam.newPageInstance());
    }

    /**
     * 返回分类列表
     *
     * @param active
     * @param vo
     * @return 返回选中的分类；当没有匹配时，返回默认的全部分类
     */
    private CategoryDTO categories(String active, IndexVo vo) {
        List<CategoryDTO> allList = categoryService.loadAllCategories();
        allList.add(0, new CategoryDTO(0L, CategoryDTO.DEFAULT_TOTAL_CATEGORY));
        if (StringUtils.isBlank(active)) {
            // 添加默认的全部分类
            vo.setCategories(allList);
            return allList.get(0);
        }

//        Map<Long, Long> articleCnt = articleReadService.queryArticleCountsByCategory();
//        allList.removeIf(c -> articleCnt.getOrDefault(c.getCategoryId(), 0L) <= 0L);

        AtomicReference<CategoryDTO> selectedArticle = new AtomicReference<>();
        allList.forEach(category -> {
            if (category.getCategory().equalsIgnoreCase(active)) {
                category.setSelected(true);
                selectedArticle.set(category);
            } else {
                category.setSelected(false);
            }
        });
        vo.setCategories(allList);
        return selectedArticle.get();
    }


    /**
     * 用户信息
     * @return
     */
    private BaseUserInfoDTO userInfo() {
        if (ReqInfoContext.getReqInfo().getUserId() != null) {
            return userService.queryBasicUserInfo(ReqInfoContext.getReqInfo().getUserId());
        }
        return null;
    }
}
