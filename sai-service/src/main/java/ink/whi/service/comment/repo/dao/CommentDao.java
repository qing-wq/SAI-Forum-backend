package ink.whi.service.comment.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.repo.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

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
}
