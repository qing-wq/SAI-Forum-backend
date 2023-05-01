package ink.whi.service.user.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.CollectionStatEnum;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.PraiseStatEnum;
import ink.whi.api.model.enums.ReadStatEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.user.dto.ArticleFootCountDTO;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.mapper.UserFootMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class UserFootDao extends ServiceImpl<UserFootMapper, UserFootDO> {

    /**
     * 查询作者全部文章计数信息
     * @param userId
     * @return
     */
    public ArticleFootCountDTO countArticleByUserId(Long userId) {
        ArticleFootCountDTO count = baseMapper.countArticleByUserId(userId);
//        Optional.ofNullable(count).ifPresent(s -> s.setReadCount(baseMapper.countArticleReadsByUserId(userId)));
        return count;
    }

    public ArticleFootCountDTO countArticleByArticleId(Long articleId) {
        return baseMapper.countArticleByArticleId(articleId);
    }

    public UserFootDO getRecordByDocumentAndUserId(DocumentTypeEnum type, Long articleId, Long userId) {
        return lambdaQuery().eq(UserFootDO::getDocumentId, articleId)
                .eq(UserFootDO::getUserId, userId)
                .eq(UserFootDO::getDocumentType, type.getCode())
                .one();
    }

    public UserFootDO getByDocumentAndUserId(Long commentId, Integer documentType, Long userId) {
        return lambdaQuery().eq(UserFootDO::getDocumentId, commentId)
                .eq(UserFootDO::getDocumentType, documentType)
                .eq(UserFootDO::getUserId, userId)
                .one();
    }

    public Integer countCommentPraise(Long commentId) {
        return lambdaQuery().eq(UserFootDO::getDocumentId, commentId)
                .eq(UserFootDO::getDocumentType, DocumentTypeEnum.COMMENT.getCode())
                .eq(UserFootDO::getPraiseStat, PraiseStatEnum.PRAISE.getCode())
                .count().intValue();
    }

    public List<SimpleUserInfoDTO> listPraiseUserByArticleId(Long articleId) {
        return baseMapper.listPraiseUserByArticleId(articleId);
    }

    public List<Long> listReadArticleByUserId(Long userId, PageParam pageParam) {
        List<UserFootDO> list = lambdaQuery().eq(UserFootDO::getUserId, userId)
                .eq(UserFootDO::getReadStat, ReadStatEnum.READ.getCode())
                .eq(UserFootDO::getDocumentType, DocumentTypeEnum.ARTICLE.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getUpdateTime)
                .list();
        return list.stream().map(UserFootDO::getDocumentId).toList();
    }

    public List<Long> listCollectedArticlesByUserId(Long userId, PageParam pageParam) {
        List<UserFootDO> list = lambdaQuery().eq(UserFootDO::getUserId, userId)
                .eq(UserFootDO::getCollectionStat, CollectionStatEnum.COLLECTION.getCode())
                .eq(UserFootDO::getDocumentType, DocumentTypeEnum.ARTICLE.getCode())
                .orderByDesc(BaseDO::getUpdateTime)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return list.stream().map(UserFootDO::getDocumentId).toList();
    }
}
