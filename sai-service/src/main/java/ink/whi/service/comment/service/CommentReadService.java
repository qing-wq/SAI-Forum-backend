package ink.whi.service.comment.service;

import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.service.comment.repo.entity.CommentDO;

import java.util.List;

/**
 * 评论读接口
 * @author: qing
 * @Date: 2023/4/28
 */
public interface CommentReadService {
    Integer queryCommentCount(Long articleId);

    List<TopCommentDTO> getArticleComments(Long articleId, PageParam newPageInstance);

    CommentDO queryComment(Long commentId);
}
