package ink.whi.service.comment.service.Impl;

import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.service.comment.repo.dao.CommentDao;
import ink.whi.service.comment.service.CommentReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam newPageInstance) {

    }
}
