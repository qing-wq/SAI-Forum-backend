package ink.whi.service.notify.repo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.api.model.vo.notify.enums.NotifyStatEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.service.notify.repo.mapper.NotifyMsgMapper;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
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
        return lambdaQuery().eq(NotifyMsgDO::getOperateUserId, notify.getOperateUserId())
                .eq(NotifyMsgDO::getNotifyUserId, notify.getNotifyUserId())
                .eq(NotifyMsgDO::getRelatedId, notify.getRelatedId())
                .eq(NotifyMsgDO::getType, notify.getType())
                .one();
    }

    /**
     * 返回用户未读消息
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
}
