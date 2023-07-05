package ink.whi.api.model.vo.comment;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class CommentSaveReq {

    /**
     * 评论ID(非必须)
     */
    private Long commentId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 顶级评论ID
     */
    private Long topCommentId;
}
