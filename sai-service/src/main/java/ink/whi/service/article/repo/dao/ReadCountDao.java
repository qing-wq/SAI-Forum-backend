package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.service.article.repo.entity.ReadCountDO;
import ink.whi.service.article.repo.mapper.ReadCountMapper;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class ReadCountDao extends ServiceImpl<ReadCountMapper, ReadCountDO> {
}
