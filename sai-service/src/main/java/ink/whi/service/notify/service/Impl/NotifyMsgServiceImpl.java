package ink.whi.service.notify.service.Impl;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import ink.whi.service.notify.service.NotifyMsgService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class NotifyMsgServiceImpl implements NotifyMsgService {

    @Autowired
    private NotifyMsgDao notifyMsgDao;

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
}
