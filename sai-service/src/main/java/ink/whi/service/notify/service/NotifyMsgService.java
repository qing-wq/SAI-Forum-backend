package ink.whi.service.notify.service;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;

import java.util.Map;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface NotifyMsgService {
    int queryUserNotifyMsgCount(Long userId);

    Map<String, Integer> queryUnreadCounts(Long userId);

    PageListVo<NotifyMsgDTO> queryUserNotices(Long loginUserId, NotifyTypeEnum typeEnum, PageParam newPageInstance);
}
