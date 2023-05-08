package ink.whi.service.statistics.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.statistic.dto.StatisticsDayDTO;
import ink.whi.service.statistics.repo.entity.RequestCountDO;

import java.util.List;

/**
 * @author qing
 * @date 2023/4/27
 */
public interface RequestCountMapper extends BaseMapper<RequestCountDO> {
    List<StatisticsDayDTO> getPvCount(Integer day);

    List<StatisticsDayDTO> getUvCount(Integer day);
}
