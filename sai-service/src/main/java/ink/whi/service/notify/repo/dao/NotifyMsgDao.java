package ink.whi.service.notify.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.service.notify.repo.mapper.NotifyMsgMapper;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class NotifyMsgDao extends ServiceImpl<NotifyMsgMapper, NotifyMsgDO> {

    /**
     * 查询用户消息数
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
     * @param notify
     * @return
     */
    public NotifyMsgDO getByUserIdRelatedIdAndType(NotifyMsgDO notify) {
        return lambdaQuery().eq(NotifyMsgDO::getNotifyUserId, notify.getNotifyUserId())
                .eq(NotifyMsgDO::getRelatedId, notify.getRelatedId())
                .eq(NotifyMsgDO::getType, notify.getType())
                .one();
    }
}
