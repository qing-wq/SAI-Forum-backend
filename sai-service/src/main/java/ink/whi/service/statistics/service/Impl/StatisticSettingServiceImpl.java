package ink.whi.service.statistics.service.Impl;

import ink.whi.service.statistics.repo.dao.RequestCountDao;
import ink.whi.service.statistics.repo.entity.RequestCountDO;
import ink.whi.service.statistics.service.StatisticSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class StatisticSettingServiceImpl implements StatisticSettingService {
    @Autowired
    private RequestCountDao requestCountDao;

    @Override
    public void saveRequestCount(String host) {
        Date date = Date.valueOf(LocalDate.now());
        RequestCountDO requestCount = requestCountDao.getRequestCount(host, date);
        if (requestCount == null) {
            // 请求不存在，保存新纪录
            RequestCountDO request = RequestCountDO.builder().host(host).cnt(1).date(date).build();
            requestCountDao.save(request);
        } else {
            requestCount.setCnt(requestCount.getCnt() + 1);
            requestCountDao.updateById(requestCount);
        }
    }
}
