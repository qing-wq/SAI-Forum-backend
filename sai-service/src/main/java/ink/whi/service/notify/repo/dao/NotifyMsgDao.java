package ink.whi.service.notify.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.notify.repo.mapper.NotifyMsgMapper;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class NotifyMsgDao extends ServiceImpl<NotifyMsgMapper, NotifyMsgDO> {
    private static final Long ADMIN_ID = 0L;

    @Autowired
    private UserDao userDao;

    /**
     * 查询用户消息数
     *
     * @param userId
     * @param statEnum
     * @return
     */
    public int countByUserIdAndStat(Long userId, NotifyStatEnum statEnum) {
        return lambdaQuery().eq(NotifyMsgDO::getNotifyUserId, userId)
                .eq(NotifyMsgDO::getState, statEnum.getStat())
                .count().intValue();
    }

    /**
     * 查询消息记录，用于幂等校验
     *
     * @param notify
     * @return
     */
    public NotifyMsgDO getByUserIdRelatedIdAndType(NotifyMsgDO notify) {
        return lambdaQuery().eq(NotifyMsgDO::getOperateUserId, notify.getOperateUserId())
                .eq(NotifyMsgDO::getNotifyUserId, notify.getNotifyUserId())
                .eq(NotifyMsgDO::getRelatedId, notify.getRelatedId())
                .eq(NotifyMsgDO::getType, notify.getType())
                .one();
    }

    /**
     * 返回用户未读消息
     *
     * @param userId
     * @param type
     * @param page
     * @return
     */
    public List<NotifyMsgDTO> listNotifyMsgByUserIdAndType(Long userId, NotifyTypeEnum type, PageParam page) {
        switch (type) {
            case REPLY:
            case COMMENT:
            case COLLECT:
            case PRAISE:
                return baseMapper.listArticleRelatedNotices(userId, type.getType(), page);
            default:
                return baseMapper.listNormalNotices(userId, type.getType(), page);
        }
    }

    /**
     * 查询各类消息数量
     * key-type  value-cnt
     *
     * @param userId
     * @param stat
     * @return
     */
    public Map<Integer, Integer> groupCountByUserIdAndStat(Long userId, Integer stat) {
        QueryWrapper<NotifyMsgDO> query = Wrappers.query();
        query.select("type, count(*) as cnt")
                .eq("notify_user_id", userId)
                .eq(stat != null, "state", stat)
                .groupBy("type");
        List<Map<String, Object>> mapList = listMaps(query);
        Map<Integer, Integer> map = new HashMap<>();
        mapList.forEach(s -> {
            Integer type = Integer.valueOf(s.get("type").toString());
            Integer cnt = Integer.valueOf(s.get("cnt").toString());
            map.put(type, cnt);
        });
        return map;
    }

    /**
     * 将消息状态设置为已读
     *
     * @param list
     */
    public void updateNotifyMsgToRead(List<NotifyMsgDTO> list) {
        List<Long> ids = list.stream().map(NotifyMsgDTO::getMsgId).toList();
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<NotifyMsgDO> record = listByIds(ids);
        List<NotifyMsgDO> notify = record.stream().map(s -> s.setState(NotifyStatEnum.READ.getStat())).toList();
        updateBatchById(notify);
    }

    public void saveCommentNotify(CommentDO comment, Long userId) {
        UserInfoDO user = userDao.getByUserId(comment.getUserId());
        String msg = String.format("%s 评论了：%s", user.getUserName(), comment.getContent());
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(comment.getArticleId())
                .msg(msg)
                .notifyUserId(userId)
                .operateUserId(comment.getUserId())
                .type(NotifyTypeEnum.COMMENT.getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        save(notify);
    }

    public void saveReplyNotify(CommentDO comment, Long parentCommentUserId) {
        UserInfoDO user = userDao.getByUserId(comment.getUserId());
        String msg = String.format("%s 回复了：%s", user.getUserName(), comment.getContent());
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(comment.getArticleId())
                .msg(msg)
                .notifyUserId(parentCommentUserId)
                .operateUserId(comment.getUserId())
                .type(NotifyTypeEnum.REPLY.getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        save(notify);
    }

    public void saveArticlePraise(UserFootDO foot) {
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .msg("用户点赞")
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(NotifyTypeEnum.PRAISE.getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        NotifyMsgDO record = getByUserIdRelatedIdAndType(notify);
        if (record == null) {
            // keypoint: 幂等过滤
            save(notify);
        }
    }

    public void saveArticleCollect(UserFootDO foot) {
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .msg("用户收藏文章")
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(NotifyTypeEnum.COLLECT.getType())
                .state(NotifyStatEnum.UNREAD.getStat()).build();
        NotifyMsgDO record = getByUserIdRelatedIdAndType(notify);
        if (record == null) {
            save(notify);
        }
    }

    public void removeArticlePraise(UserFootDO foot) {
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .msg("用户取消点赞")
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(NotifyTypeEnum.CANCEL_PRAISE.getType()).build();
        NotifyMsgDO record = getByUserIdRelatedIdAndType(notify);
        if (record == null) {
            removeById(record.getId());
        }
    }

    public void removeArticleCollect(UserFootDO foot) {
        NotifyMsgDO notify = NotifyMsgDO.builder().relatedId(foot.getDocumentId())
                .msg("用户取消收藏")
                .notifyUserId(foot.getDocumentUserId())
                .operateUserId(foot.getUserId())
                .type(NotifyTypeEnum.CANCEL_COLLECT.getType()).build();
        NotifyMsgDO record = getByUserIdRelatedIdAndType(notify);
        if (record == null) {
            removeById(record.getId());
        }
    }

    public void saveFollowNotify(UserRelationDO relation) {
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(NotifyTypeEnum.FOLLOW.getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("用户关注");
        NotifyMsgDO record = getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            save(msg);
        }
    }

    public void removeFollowNotify(UserRelationDO relation) {
        NotifyMsgDO msg = new NotifyMsgDO()
                .setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(NotifyTypeEnum.CANCEL_FOLLOW.getType())
                .setMsg("用户取消关注");
        NotifyMsgDO record = getByUserIdRelatedIdAndType(msg);
        if (record != null) {
            removeById(record.getId());
        }
    }

    public void saveRegisterSystemNotify(Long userId) {
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(userId)
                .setOperateUserId(ADMIN_ID)
                .setType(NotifyTypeEnum.REGISTER.getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg(SpringUtil.getConfig("msg.welcome"));
        NotifyMsgDO record = getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            save(msg);
        }
    }
}
