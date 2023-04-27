package ink.whi.service.user.converter;

import ink.whi.api.model.enums.RoleEnum;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.service.user.repo.entity.UserInfoDO;
import org.springframework.beans.BeanUtils;

/**
 * 实体转换
 * @author: qing
 * @Date: 2023/4/26
 */
public class UserConverter {

    public static BaseUserInfoDTO toDTO(UserInfoDO info) {
        if (info == null) {
            return null;
        }
        BaseUserInfoDTO user = new BaseUserInfoDTO();
        BeanUtils.copyProperties(info, user);
        user.setRegion(info.getIp().getLatestRegion());
        user.setRole(RoleEnum.role(info.getUserRole()));
        return user;
    }

    public static UserStatisticInfoDTO toUserHomeDTO(BaseUserInfoDTO baseUserInfoDTO) {
        if (baseUserInfoDTO == null) {
            return null;
        }
        UserStatisticInfoDTO userHomeDTO = new UserStatisticInfoDTO();
        BeanUtils.copyProperties(baseUserInfoDTO, userHomeDTO);
        return userHomeDTO;
    }
}
