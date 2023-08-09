package ink.whi.service.comment.service.Impl;

import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.comment.CommentSaveReq;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.JsonUtil;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.converter.CommentConverter;
import ink.whi.service.comment.repo.dao.CommentDao;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentWriteService;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@Service
public class CommentWriteServiceImpl implements CommentWriteService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveComment(CommentSaveReq req) {
        CommentDO comment = null;
        if (req.getCommentId() == null) {
            comment = addComment(req);
        } else {
            comment = updateComment(req);
        }
        return comment.getId();
    }

    private CommentDO addComment(CommentSaveReq req) {
        ArticleDO article = articleReadService.queryBasicArticle(req.getArticleId());
        if (article == null) {
            throw BusinessException.newInstance(StatusEnum.ARTICLE_NOT_EXISTS, "文章不存在：" + req.getArticleId());
        }
        CommentDO comment = CommentConverter.toDo(req);
        commentDao.save(comment);

        Long parentCommentUser = getParentComment(comment);
        // 保存足迹
        userFootService.saveCommentFoot(comment, article.getUserId(), parentCommentUser);

        // 发布事件
        rabbitmqService.publishMsg(JsonUtil.toStr(new RabbitmqMsg<>(NotifyTypeEnum.COMMENT, comment)));
        if (NumUtil.upZero(parentCommentUser)){
            rabbitmqService.publishMsg(JsonUtil.toStr(new RabbitmqMsg<>(NotifyTypeEnum.REPLY, comment)));
        }
        return comment;
    }

    private CommentDO updateComment(CommentSaveReq req) {
        CommentDO record = commentDao.getById(req.getCommentId());
        if (record == null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "评论不存在：" + req.getCommentId());
        }
        record.setContent(req.getCommentContent());
        record.setUpdateTime(new Date());
        commentDao.updateById(record);
        return record;
    }

    /**
     * 获取父评论用户
     * @param comment
     * @return
     */
    private Long getParentComment(CommentDO comment) {
        if (!NumUtil.upZero(comment.getParentCommentId())) {
            return null;
        }
        CommentDO parent = commentDao.getById(comment.getParentCommentId());
        if (parent == null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "父评论不存在：" + comment.getParentCommentId());
        }
        return parent.getUserId();
    }
}
