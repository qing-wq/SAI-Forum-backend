package ink.whi.web.article.rest;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.web.base.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章列表接口
 * @author: qing
 * @Date: 2023/4/29
 */
@RestController
@RequestMapping(path = "article/api/list")
public class ArticleListRestController extends BaseRestController {

    @Autowired
    private ArticleReadService articleReadService;

    /**
     * 文章分类分页查询接口
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(path = "category/{category}")
    public ResVo<PageListVo<ArticleDTO>> category(@PathVariable(name = "category") Long category,
                                      @RequestParam(name = "page") Long pageNum,
                                      @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        PageListVo<ArticleDTO> list = articleReadService.queryArticlesByCategory(category, pageParam);
        return ResVo.ok(list);
    }

    /**
     * 相关文章推荐接口
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(path = "recommend/{articleId}")
    public ResVo<PageListVo<ArticleDTO>> recommend(@PathVariable(name = "articleId") Long articleId,
                                                   @RequestParam(name = "page") Long pageNum,
                                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        PageListVo<ArticleDTO> list = articleReadService.queryRecommendArticle(articleId, pageParam);
        return ResVo.ok(list);
    }
}
