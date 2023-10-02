package ink.whi.service.statistics.service;

import ink.whi.api.model.vo.statistic.dto.StatisticsCountDTO;
import ink.whi.api.model.vo.statistic.dto.StatisticsDayDTO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface StatisticsSettingService {
    void saveRequestCount(String ip);

    StatisticsCountDTO getStatisticsCount();

    List<StatisticsDayDTO> getPvAndUvDayList(Integer day);

    List<StatisticsDayDTO> getUvDayList(Integer day);
}
