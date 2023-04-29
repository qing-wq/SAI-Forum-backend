package ink.whi.service.notify.service.Impl;

import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.service.notify.repo.dao.NotifyMsgDao;
import ink.whi.service.notify.service.NotifyMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
