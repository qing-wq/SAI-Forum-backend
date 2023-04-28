package ink.whi.service.comment.service;

/**
 * 评论读接口
 * @author: qing
 * @Date: 2023/4/28
 */
public interface CommentReadService {
    Integer queryCommentCount(Long articleId);
}
