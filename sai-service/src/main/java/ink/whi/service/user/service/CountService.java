package ink.whi.service.user.service;

import ink.whi.api.model.vo.article.dto.ArticleFootCountDTO;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface CountService {
    /**
     * 查询作者全部文章计数的统计
     * @param userId
     * @return
     */
    ArticleFootCountDTO queryArticleCountInfoByUserId(Long userId);

    /**
     * 文章点赞、阅读、评论、收藏
     * @param articleId
     * @return
     */
    ArticleFootCountDTO queryArticleCountInfoByArticleId(Long articleId);

    /**
     * 查询点赞数
     * @param commentId
     * @return
     */
    Integer queryCommentPraiseCount(Long commentId);
}
