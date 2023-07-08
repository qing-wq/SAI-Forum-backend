package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.DraftDTO;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.entity.DraftDO;
import ink.whi.service.article.repo.mapper.DraftMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/7/7
 */
@Repository
public class DraftDao extends ServiceImpl<DraftMapper, DraftDO> {
    /**
     * 获取草稿箱列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    public PageListVo<DraftDTO> listDrafts(Long userId, PageParam pageParam) {
        List<DraftDO> list = lambdaQuery().select(BaseDO::getId, DraftDO::getAuthor, DraftDO::getTitle, BaseDO::getCreateTime, BaseDO::getUpdateTime)
                .eq(DraftDO::getAuthor, userId)
                .eq(DraftDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(BaseDO::getCreateTime)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return PageListVo.newVo(ArticleConverter.toDraftList(list), pageParam.getPageSize());
    }

    /**
     * 查找文章草稿记录
     *
     * @param articleId
     * @return
     */
    public DraftDO findLastDraft(Long articleId) {
        return lambdaQuery().eq(DraftDO::getArticleId, articleId)
                .eq(DraftDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
    }

    public DraftDTO queryDraftById(Long draftId) {
        DraftDO draft = lambdaQuery().eq(BaseDO::getId, draftId)
                .eq(DraftDO::getDeleted, YesOrNoEnum.NO.getCode())
                .one();
        return ArticleConverter.toDto(draft);
    }

    /**
     * 删除文章草稿记录
     * @param articleId
     */
    public void deletedArticleDraft(Long articleId) {
        DraftDO record = lambdaQuery().eq(DraftDO::getArticleId, articleId)
                .eq(DraftDO::getDeleted, YesOrNoEnum.NO.getCode()).one();
        record.setDeleted(YesOrNoEnum.YES.getCode());
    }
}
