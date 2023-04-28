package ink.whi.service.comment.service.Impl;

import ink.whi.service.comment.repo.dao.CommentDao;
import ink.whi.service.comment.service.CommentReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Service
public class CommentReadServiceImpl implements CommentReadService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public Integer queryCommentCount(Long articleId) {
        return commentDao.countCommentByArticleId(articleId);
    }
}
