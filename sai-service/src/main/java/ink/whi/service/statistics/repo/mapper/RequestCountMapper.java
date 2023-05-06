package ink.whi.service.statistics.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.service.statistics.repo.entity.RequestCountDO;
import ink.whi.web.admin.statistics.vo.StatisticsDayVO;

import java.util.List;

/**
 * @author qing
 * @date 2023/4/27
 */
public interface RequestCountMapper extends BaseMapper<RequestCountDO> {
    List<StatisticsDayVO> getPvCount(Integer day);

    List<StatisticsDayVO> getUvCount(Integer day);
}
