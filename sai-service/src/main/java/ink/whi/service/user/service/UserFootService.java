package ink.whi.service.user.service;

import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.service.user.repo.entity.UserFootDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public interface UserFootService {
    UserFootDO saveOrUpdateUserFoot(DocumentTypeEnum article, Long articleId, Long author, Long userId, OperateTypeEnum read);

    UserFootDO queryUserFoot(Long commentId, Integer code, Long userId);

    List<SimpleUserInfoDTO> queryArticlePraisedUsers(Long articleId);
}
