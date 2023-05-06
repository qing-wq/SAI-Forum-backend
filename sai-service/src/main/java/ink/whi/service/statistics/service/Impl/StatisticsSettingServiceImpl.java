package ink.whi.service.statistics.service.Impl;

import ink.whi.service.article.service.ArticleSettingsService;
import ink.whi.service.statistics.repo.dao.RequestCountDao;
import ink.whi.service.statistics.repo.entity.RequestCountDO;
import ink.whi.service.statistics.service.StatisticsSettingService;
import ink.whi.service.user.service.UserSettingService;
import ink.whi.web.admin.statistics.vo.StatisticsCountVO;
import ink.whi.web.admin.statistics.vo.StatisticsDayVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class StatisticsSettingServiceImpl implements StatisticsSettingService {
    @Autowired
    private RequestCountDao requestCountDao;
    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private ArticleSettingsService articleSettingsService;

    @Override
    public void saveRequestCount(String host) {
        Date date = Date.valueOf(LocalDate.now());
        RequestCountDO requestCount = requestCountDao.getRequestCount(host, date);
        if (requestCount == null) {
            // 请求不存在，保存新纪录
            RequestCountDO request = RequestCountDO.builder().host(host).cnt(1).date(date).build();
            requestCountDao.save(request);
        } else {
            // todo：存在并发情况
            requestCount.setCnt(requestCount.getCnt() + 1);
            requestCountDao.updateById(requestCount);
        }
    }

    /**
     * 获取后台统计计数
     * @return
     */
    @Override
    public StatisticsCountVO getStatisticsCount() {
        Integer userCount = userSettingService.getUserCount();
        Integer articleCount = articleSettingsService.getArticleCount();
        Integer pvTotalCount = requestCountDao.getPvTotalCount();

        return StatisticsCountVO.builder().articleCount(articleCount).userCount(userCount).pvCount(pvTotalCount).build();
    }

    /**
     * 获取最近day天的pv值
     * @param day
     * @return
     */
    @Override
    public List<StatisticsDayVO> getPvDayList(Integer day) {
        return requestCountDao.getPvDayList(day);
    }

    @Override
    public List<StatisticsDayVO> getUvDayList(Integer day) {
        return requestCountDao.getUvDayList(day);
    }
}
