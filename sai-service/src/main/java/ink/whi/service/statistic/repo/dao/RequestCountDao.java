package ink.whi.service.statistic.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.service.statistic.repo.entity.RequestCountDO;
import ink.whi.service.statistic.repo.mapper.RequestCountMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class RequestCountDao extends ServiceImpl<RequestCountMapper, RequestCountDO> {

    /**
     * 根据ip和日期查询请求
     * @param ip
     * @param date
     */
    public RequestCountDO getRequestCount(String ip, Date date) {
        return lambdaQuery().eq(RequestCountDO::getHost, ip)
                .eq(RequestCountDO::getDate, date)
                .one();
    }
}
