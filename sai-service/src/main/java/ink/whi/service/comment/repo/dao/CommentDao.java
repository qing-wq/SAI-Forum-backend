package ink.whi.service.comment.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.repo.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class CommentDao extends ServiceImpl<CommentMapper, CommentDO> {
    /**
     * 查询文章评论数量
     * @param articleId
     * @return
     */
    public Integer countCommentByArticleId(Long articleId) {
        return lambdaQuery().eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }

    /**
     * 顶级评论
     * @param articleId
     * @param page
     * @return
     */
    public List<CommentDO> listTopCommentList(Long articleId, PageParam page) {
        return lambdaQuery()
                .eq(CommentDO::getTopCommentId, 0)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(page))
                .orderByDesc(CommentDO::getId).list();
    }

    /**
     * 非顶级评论
     * @param articleId
     * @param topCommentIds
     * @return
     */
    public List<CommentDO> listSubCommentIdMappers(Long articleId, Set<Long> topCommentIds) {
        return lambdaQuery()
                .in(CommentDO::getTopCommentId, topCommentIds)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode()).list();
    }
}
