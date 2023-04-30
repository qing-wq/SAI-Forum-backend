package ink.whi.service.article.service;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.service.article.repo.entity.ArticleDO;

import java.util.List;
import java.util.Map;

/**
 * 文章读接口
 *
 * @author: qing
 * @Date: 2023/4/27
 */
public interface ArticleReadService {
    PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam newPageInstance);

    PageListVo<ArticleDTO> buildArticleListVo(List<ArticleDO> records, long pageSize);

    /**
     * 查询所有分类对应的文章数
     * @return
     */
    Map<Long, Long> queryArticleCountsByCategory();

    ArticleDTO queryTotalArticleInfo(Long articleId, Long readUser);

    Integer queryArticleCount(Long userId);

    ArticleDO queryBasicArticle(Long articleId);

    PageListVo<ArticleDTO> queryRecommendArticle(Long articleId, PageParam pageParam);

    ArticleDTO queryDetailArticleInfo(Long articleId);
}
