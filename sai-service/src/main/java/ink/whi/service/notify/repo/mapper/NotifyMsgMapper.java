package ink.whi.service.notify.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.notify.dto.NotifyMsgDTO;
import ink.whi.service.notify.repo.entity.NotifyMsgDO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface NotifyMsgMapper extends BaseMapper<NotifyMsgDO> {
    /**
     * 文章相关的消息
     * @param userId
     * @param type
     * @param page
     * @return
     */
    List<NotifyMsgDTO> listArticleRelatedNotices(Long userId, Integer type, PageParam pageParam);

    /**
     * 普通消息，如关注等
     * @param userId
     * @param type
     * @param page
     * @return
     */
    List<NotifyMsgDTO> listNormalNotices(Long userId, Integer type, PageParam pageParam);
}
