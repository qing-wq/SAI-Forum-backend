package ink.whi.service.comment.service;

import ink.whi.api.model.vo.comment.CommentSaveReq;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
public interface CommentWriteService {
    Long saveComment(CommentSaveReq req);
}
