package ink.whi.service.notify.service.notify;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.NumUtil;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import ink.whi.service.notify.service.NotifyMsgService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserRelationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class NotifyMsgServiceImpl implements NotifyMsgService {

    @Autowired
    private NotifyMsgDao notifyMsgDao;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CommentReadService commentReadService;

    /**
     * 查询用户未读消息数
     * @param userId
     * @return
     */
    @Override
    public int queryUserNotifyMsgCount(Long userId) {
        return notifyMsgDao.countByUserIdAndStat(userId, NotifyStatEnum.UNREAD);
    }

    /**
     * 查询用户各类未读消息数
     * @param userId
     * @return
     */
    @Override
    public Map<String, Integer> queryUnreadCounts(Long userId) {
        if (!NumUtil.upZero(ReqInfoContext.getReqInfo().getMsgNum())) {
            return Collections.emptyMap();
        }
        Map<Integer, Integer> map = notifyMsgDao.groupCountByUserIdAndStat(userId, NotifyStatEnum.UNREAD.getStat());
        // 指定先后顺序
        Map<String, Integer> ans = new LinkedHashMap<>();
        initCnt(NotifyTypeEnum.COMMENT, map, ans);
        initCnt(NotifyTypeEnum.REPLY, map, ans);
        initCnt(NotifyTypeEnum.PRAISE, map, ans);
        initCnt(NotifyTypeEnum.COLLECT, map, ans);
        initCnt(NotifyTypeEnum.FOLLOW, map, ans);
        initCnt(NotifyTypeEnum.SYSTEM, map, ans);
        return ans;
    }

    private void initCnt(NotifyTypeEnum type, Map<Integer, Integer> map, Map<String, Integer> result) {
        result.put(type.name().toLowerCase(), map.getOrDefault(type.getType(), 0));
    }

    /**
     * 根据消息类型查询用户消息
     * @param userId
     * @param typeEnum
     * @param page
     * @return
     */
    @Override
    public PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum typeEnum, PageParam page) {
        List<NotifyMsgDTO> list = notifyMsgDao.listNotifyMsgByUserIdAndType(userId, typeEnum, page);
        // 将消息设为已读
        notifyMsgDao.updateNotifyMsgToRead(list);
        // 更新全局总的消息数
        ReqInfoContext.getReqInfo().setMsgNum(queryUserNotifyMsgCount(userId));
        // todo：更新用户关系
        return PageListVo.newVo(list, page.getPageSize());
    }

    /**
     * 用户评论消息
     * @param comment
     */
    @Override
    public void saveCommentNotify(CommentDO comment) {
        ArticleDO article = articleReadService.queryBasicArticle(comment.getArticleId());
        notifyMsgDao.saveCommentNotify(comment, article.getUserId());
    }

    /**
     * 用户回复消息
     * @param comment
     */
    @Override
    public void saveReplyNotify(CommentDO comment) {
        CommentDO parentComment = commentReadService.queryComment(comment.getParentCommentId());
        notifyMsgDao.saveReplyNotify(comment, parentComment.getUserId());
    }

    /**
     * 文章点赞消息
     * @param foot
     */
    @Override
    public void saveArticlePraise(UserFootDO foot){
        notifyMsgDao.saveArticlePraise(foot);
    }

    /**
     * 文章收藏消息
     * @param foot
     */
    @Override
    public void saveArticleCollect(UserFootDO foot){
        notifyMsgDao.saveArticleCollect(foot);
    }

    /**
     * 文章取消点赞消息
     * @param foot
     */
    @Override
    public void removeArticlePraise(UserFootDO foot) {
        notifyMsgDao.removeArticlePraise(foot);
    }

    /**
     * 文章取消收藏消息
     * @param foot
     */
    @Override
    public void removeArticleCollect(UserFootDO foot) {
        notifyMsgDao.removeArticleCollect(foot);
    }

    /**
     * 用户关注消息
     * @param relation
     */
    @Override
    public void saveFollowNotify(UserRelationDO relation) {
        notifyMsgDao.saveFollowNotify(relation);
    }

    /**
     * 用户取消关注消息
     * @param relation
     */
    @Override
    public void removeFollowNotify(UserRelationDO relation) {
        notifyMsgDao.removeFollowNotify(relation);
    }

    /**
     * 用户注册系统消息
     * @param userId
     */
    @Override
    public void saveRegisterSystemNotify(Long userId) {
        notifyMsgDao.saveRegisterSystemNotify(userId);
    }
}