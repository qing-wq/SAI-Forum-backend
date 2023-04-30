package ink.whi.service.statistics.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.service.statistics.repo.entity.RequestCountDO;
import ink.whi.service.statistics.repo.mapper.RequestCountMapper;
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
     * @param host
     * @param date
     */
    public RequestCountDO getRequestCount(String host, Date date) {
        return lambdaQuery().eq(RequestCountDO::getHost, host)
                .eq(RequestCountDO::getDate, date)
                .one();
    }
}
