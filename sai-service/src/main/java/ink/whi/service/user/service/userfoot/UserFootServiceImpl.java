package ink.whi.service.user.service.userfoot;

import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Service
public class UserFootServiceImpl implements UserFootService {
    @Autowired
    private UserFootDao userFootDao;

    @Override
    public UserFootDO saveOrUpdateUserFoot(DocumentTypeEnum type, Long articleId, Long author, Long readUser, OperateTypeEnum operateTypeEnum) {
        UserFootDO record = userFootDao.getRecordByDocumentAndUserId(type, articleId, readUser);
        if (record == null) {
            record = new UserFootDO();
            record.setUserId(readUser);
            record.setDocumentId(articleId);
            record.setDocumentType(type.getCode());
            record.setDocumentUserId(author);
            setUserFootStat(record, operateTypeEnum);
            userFootDao.save(record);
        } else if (setUserFootStat(record, operateTypeEnum)) {
            record.setUpdateTime(new Date());
            userFootDao.updateById(record);
        }
        return record;
    }

    @Override
    public UserFootDO queryUserFoot(Long commentId, Integer documentType, Long userId) {
        return userFootDao.getByDocumentAndUserId(commentId, documentType, userId);
    }

    /**
     * 查询文章点赞用户
     * @param articleId
     * @return
     */
    @Override
    public List<SimpleUserInfoDTO> queryArticlePraisedUsers(Long articleId) {
        return userFootDao.listPraiseUserByArticleId(articleId);
    }

    private boolean setUserFootStat(UserFootDO userFootDO, OperateTypeEnum operate) {
        switch (operate) {
            case READ:
                // 设置为已读
                userFootDO.setReadStat(1);
                // 需要更新时间，用于浏览记录
                return true;
            case PRAISE:
            case CANCEL_PRAISE:
                return compareAndUpdate(userFootDO::getPraiseStat, userFootDO::setPraiseStat, operate.getDbStatCode());
            case COLLECTION:
            case CANCEL_COLLECTION:
                return compareAndUpdate(userFootDO::getCollectionStat, userFootDO::setCollectionStat, operate.getDbStatCode());
            case COMMENT:
            case DELETE_COMMENT:
                return compareAndUpdate(userFootDO::getCommentStat, userFootDO::setCommentStat, operate.getDbStatCode());
            default:
                return false;
        }
    }

    private <T> boolean compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return false;
        }
        consumer.accept(input);
        return true;
    }
}
