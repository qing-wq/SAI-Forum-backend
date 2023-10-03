package ink.whi.service.user.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.user.dto.StatisticUserInfoDTO;
import ink.whi.service.user.repo.entity.UserInfoDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {
    List<StatisticUserInfoDTO> getUserList(PageParam pageParam);
}
