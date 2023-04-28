package ink.whi.service.user.service;

import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.service.user.repo.entity.UserFootDO;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public interface UserFootService {
    UserFootDO saveOrUpdateUserFoot(DocumentTypeEnum article, Long articleId, Long author, Long readUser, OperateTypeEnum read);
}
