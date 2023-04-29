package ink.whi.service.user.service.count;

import ink.whi.api.model.vo.user.dto.ArticleFootCountDTO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 计数服务
 * todo: 计数相关后续使用redis来做
 *
 * @author qing
 * @date 2023/4/28
 */
@Service
public class CountServiceImpl implements CountService {

    private final UserFootDao userFootDao;

    @Autowired
    private CommentReadService commentReadService;

    public CountServiceImpl(UserFootDao userFootDao) {
        this.userFootDao = userFootDao;
    }

    @Override
    public ArticleFootCountDTO queryArticleCountInfoByUserId(Long userId) {
        return userFootDao.countArticleByUserId(userId);
    }

    @Override
    public ArticleFootCountDTO queryArticleCountInfoByArticleId(Long articleId) {
        ArticleFootCountDTO res = userFootDao.countArticleByArticleId(articleId);
        if (res == null) {
            res = new ArticleFootCountDTO();
        } else {
            res.setCommentCount(commentReadService.queryCommentCount(articleId));
        }
        return res;
    }

    @Override
    public Integer queryCommentPraiseCount(Long commentId) {
        return userFootDao.countCommentPraise(commentId);
    }
}
