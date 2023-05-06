package ink.whi.service.statistics.service;

import ink.whi.web.admin.statistics.vo.StatisticsCountVO;
import ink.whi.web.admin.statistics.vo.StatisticsDayVO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface StatisticsSettingService {
    void saveRequestCount(String ip);

    StatisticsCountVO getStatisticsCount();

    List<StatisticsDayVO> getPvDayList(Integer day);

    List<StatisticsDayVO> getUvDayList(Integer day);
}
