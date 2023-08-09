package ink.whi.service.notify.service;

import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.entity.UserRelationDO;

import java.util.Map;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface NotifyMsgService {
    int queryUserNotifyMsgCount(Long userId);

    Map<String, Integer> queryUnreadCounts(Long userId);

    PageListVo<NotifyMsgDTO> queryUserNotices(Long loginUserId, NotifyTypeEnum typeEnum, PageParam newPageInstance);

    void saveCommentNotify(CommentDO comment);

    void saveReplyNotify(CommentDO comment);

    void saveArticlePraise(UserFootDO foot);

    void saveArticleCollect(UserFootDO foot);

    void removeArticlePraise(UserFootDO foot);

    void removeArticleCollect(UserFootDO foot);

    void saveFollowNotify(UserRelationDO relation);

    void removeFollowNotify(UserRelationDO relation);

    void saveRegisterSystemNotify(Long userId);
}
