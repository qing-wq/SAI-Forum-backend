package ink.whi.service.statistic.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.service.statistic.repo.entity.RequestCountDO;
import ink.whi.service.statistic.repo.mapper.RequestCountMapper;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class RequestCountDao extends ServiceImpl<RequestCountMapper, RequestCountDO> {
}
