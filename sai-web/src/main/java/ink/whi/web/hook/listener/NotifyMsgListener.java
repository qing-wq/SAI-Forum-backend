package ink.whi.web.hook.listener;

import ink.whi.api.model.vo.notify.NotifyMsgEvent;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 消息事件监听器
 *
 * @author: qing
 * @Date: 2023/5/4
 */
@Component
public class NotifyMsgListener<T> implements ApplicationListener<NotifyMsgEvent<T>> {

    private static final Long ADMIN_ID = 0L;
    private final NotifyMsgDao notifyMsgDao;
    private final ArticleReadService articleReadService;
    private final CommentReadService commentReadService;

    public NotifyMsgListener(NotifyMsgDao notifyMsgDao, ArticleReadService articleReadService, CommentReadService commentReadService) {
        this.notifyMsgDao = notifyMsgDao;
        this.articleReadService = articleReadService;
        this.commentReadService = commentReadService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(NotifyMsgEvent<T> msgEvent) {
        switch (msgEvent.getNotifyType()) {
            case COMMENT:
                saveCommentNotify((NotifyMsgEvent<CommentDO>) msgEvent);
                break;
            case REPLY:
                saveReplyNotify((NotifyMsgEvent<CommentDO>) msgEvent);
                break;
            case PRAISE:
            case COLLECT:
                saveArticleNotify((NotifyMsgEvent<UserFootDO>) msgEvent);
                break;
            case CANCEL_PRAISE:
            case CANCEL_COLLECT:
                removeArticleNotify((NotifyMsgEvent<UserFootDO>) msgEvent);
                break;
            case FOLLOW:
                saveFollowNotify((NotifyMsgEvent<UserRelationDO>) msgEvent);
                break;
            case CANCEL_FOLLOW:
                removeFollowNotify((NotifyMsgEvent<UserRelationDO>) msgEvent);
                break;
            case REGISTER:
                // 首次注册，插入一个欢迎的消息
                saveRegisterSystemNotify((Long) msgEvent.getContent());
                break;
            default:
                // todo 系统消息
        }
    }

    private void saveCommentNotify(NotifyMsgEvent<CommentDO> msgEvent) {
        CommentDO comment = msgEvent.getContent();
        ArticleDO article = articleReadService.queryBasicArticle(comment.getArticleId());
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(comment.getArticleId())
                .msg(comment.getContent())
                .notifyUserId(article.getUserId())
                .operateUserId(comment.getUserId())
                .type(msgEvent.getNotifyType().getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        notifyMsgDao.save(notify);
    }

    private void saveReplyNotify(NotifyMsgEvent<CommentDO> msgEvent) {
        CommentDO comment = msgEvent.getContent();
        CommentDO parentComment = commentReadService.queryComment(comment.getParentCommentId());
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(comment.getArticleId())
                .msg(comment.getContent())
                .notifyUserId(parentComment.getUserId())
                .operateUserId(comment.getUserId())
                .type(msgEvent.getNotifyType().getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        notifyMsgDao.save(notify);
    }

    private void saveArticleNotify(NotifyMsgEvent<UserFootDO> msgEvent) {
        UserFootDO foot = msgEvent.getContent();
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .msg("")
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(msgEvent.getNotifyType().getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(notify);
        if (record != null) {
            // keypoint: 幂等过滤
            notifyMsgDao.save(notify);
        }
    }

    private void removeArticleNotify(NotifyMsgEvent<UserFootDO> msgEvent) {
        UserFootDO foot = msgEvent.getContent();
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(msgEvent.getNotifyType().getType()).build();
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(notify);
        if (record != null) {
            notifyMsgDao.removeById(record.getId());
        }
    }

    private void saveFollowNotify(NotifyMsgEvent<UserRelationDO> msgEvent) {
        UserRelationDO relation = msgEvent.getContent();
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(msgEvent.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            // 幂等过滤
            notifyMsgDao.save(msg);
        }
    }

    private void removeFollowNotify(NotifyMsgEvent<UserRelationDO> msgEvent) {
        UserRelationDO relation = msgEvent.getContent();
        NotifyMsgDO msg = new NotifyMsgDO()
                .setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(msgEvent.getNotifyType().getType())
                .setMsg("");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record != null) {
            notifyMsgDao.removeById(record.getId());
        }
    }

    private void saveRegisterSystemNotify(Long userId) {
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(userId)
                .setOperateUserId(ADMIN_ID)
                .setType(NotifyTypeEnum.REGISTER.getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg(SpringUtil.getConfig("config"));
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            notifyMsgDao.save(msg);
        }
    }

}
