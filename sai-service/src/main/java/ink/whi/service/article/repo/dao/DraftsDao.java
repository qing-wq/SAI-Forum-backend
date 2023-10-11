package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.DraftsTypeEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.service.article.repo.entity.DraftsDO;
import ink.whi.service.article.repo.mapper.DraftsMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/10/10
 */
@Repository
public class DraftsDao extends ServiceImpl<DraftsMapper, DraftsDO> {


    public DraftsDO getArticleDraftByArticleId(Long articleId) {
        return lambdaQuery().eq(DraftsDO::getArticleId, articleId)
                .eq(DraftsDO::getDraftType, DraftsTypeEnum.ARTICLE.getCode())
                .eq(DraftsDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
    }

    public List<DraftsDO> listDraftByUserId(Long userId, PageParam pageParam) {
        return lambdaQuery().eq(DraftsDO::getUserId, userId)
                .eq(DraftsDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(DraftsDO::getDraftType, DraftsTypeEnum.COMMON.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(BaseDO::getUpdateTime)
                .list();
    }
}
