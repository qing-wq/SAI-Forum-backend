package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.service.article.repo.entity.DraftsDO;
import ink.whi.service.article.repo.mapper.DraftsMapper;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/10/10
 */
@Repository
public class DraftsDao extends ServiceImpl<DraftsMapper, DraftsDO> {


    public DraftsDO getArticleDraftById(Long articleId) {
        return lambdaQuery().eq(DraftsDO::getArticleId, articleId)
                .eq(DraftsDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
    }
}
