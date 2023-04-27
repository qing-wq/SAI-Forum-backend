package ink.whi.service.user.service.user;

import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.utils.IpUtil;
import ink.whi.service.user.repo.dao.UserDao;
import ink.whi.service.user.repo.entity.IpInfo;
import ink.whi.service.user.repo.entity.UserInfoDO;
import ink.whi.service.user.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Component
public class SessionServiceImpl implements SessionService {

    @Autowired
    private UserDao userDao;

    @Override
    public void updateUserIpInfo(BaseUserInfoDTO user, String clientIp) {
        UserInfoDO userInfo = userDao.getByUserId(user.getUserId());
        IpInfo ip = userInfo.getIp();
        if (Objects.equals(ip.getLatestIp(), clientIp)) {
            ip.setLatestIp(clientIp);
            ip.setLatestRegion(IpUtil.getLocationByIp(clientIp).toRegionStr());
        }

        if (ip.getFirstIp() == null) {
            ip.setFirstIp(clientIp);
            ip.setFirstRegion(IpUtil.getLocationByIp(clientIp).toRegionStr());
        }
    }
}
