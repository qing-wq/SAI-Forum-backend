package ink.whi.service.notify.service.notify;

import com.fasterxml.jackson.core.type.TypeReference;
import ink.whi.api.model.vo.notify.RabbitmqMsg;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.JsonUtil;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import ink.whi.service.notify.service.ProcessMsgService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import org.springframework.stereotype.Service;

/**
 * Rabbitmq 消息处理
 * @author: qing
 * @Date: 2023/8/2
 */
@Service
public class ProcessMsgServiceImpl implements ProcessMsgService {

    private static final Long ADMIN_ID = 0L;
    private final NotifyMsgDao notifyMsgDao;
    private final ArticleReadService articleReadService;
    private final CommentReadService commentReadService;

    public ProcessMsgServiceImpl(NotifyMsgDao notifyMsgDao, ArticleReadService articleReadService, CommentReadService commentReadService) {
        this.notifyMsgDao = notifyMsgDao;
        this.articleReadService = articleReadService;
        this.commentReadService = commentReadService;
    }

    @Override
    public void processMsg(String message, RabbitmqMsg notify) {
        switch (notify.getNotifyType()) {
            case COMMENT:
                saveCommentNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case REPLY:
                saveReplyNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case PRAISE:
            case COLLECT:
                saveArticleNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case CANCEL_PRAISE:
            case CANCEL_COLLECT:
                removeArticleNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case FOLLOW:
                saveFollowNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case CANCEL_FOLLOW:
                removeFollowNotify(JsonUtil.toObj(message, new TypeReference<>(){}));
                break;
            case REGISTER:
                // 首次注册，插入一个欢迎的消息
                saveRegisterSystemNotify((Long) notify.getContent());
                break;
            default:
                // todo 系统消息
        }

    }

    private void saveCommentNotify(RabbitmqMsg<CommentDO> msgEvent) {
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

    private void saveReplyNotify(RabbitmqMsg<CommentDO> msgEvent) {
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

    private void saveArticleNotify(RabbitmqMsg<UserFootDO> msgEvent){
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

    private void removeArticleNotify(RabbitmqMsg<UserFootDO> msgEvent) {
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

    private void saveFollowNotify(RabbitmqMsg<UserRelationDO> msgEvent) {
        UserRelationDO relation = msgEvent.getContent();
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(msgEvent.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("关注用户");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            // 幂等过滤
            notifyMsgDao.save(msg);
        }
    }

    private void removeFollowNotify(RabbitmqMsg<UserRelationDO> msgEvent) {
        UserRelationDO relation = msgEvent.getContent();
        NotifyMsgDO msg = new NotifyMsgDO()
                .setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(msgEvent.getNotifyType().getType());
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
                .setMsg(SpringUtil.getConfig("msg.welcome"));
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            notifyMsgDao.save(msg);
        }
    }
}
