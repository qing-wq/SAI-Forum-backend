package ink.whi.service.article.service;

import ink.whi.api.model.enums.HomeSelectEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.dto.SimpleArticleDTO;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
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

    PageListVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select);

    List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key);

    String generateSummary(String content);

    DraftsDTO getOnlineArticleDraft(Long articleId);

    DraftsDTO queryDraftById(Long draftId);

    List<SimpleArticleDTO> queryArticleCluster(Map<String, String> params);
}
