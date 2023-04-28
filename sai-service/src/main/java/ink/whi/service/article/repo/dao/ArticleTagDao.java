package ink.whi.service.article.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.repo.entity.ArticleTagDO;
import ink.whi.service.article.repo.mapper.ArticleTagMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Repository
public class ArticleTagDao extends ServiceImpl<ArticleTagMapper, ArticleTagDO> {
    /**
     * 查询文章标签详情
     * @param articleId
     * @return
     */
    public List<TagDTO> queryArticleTagDetails(Long articleId) {
        return baseMapper.listArticleTagDetails(articleId);
    }
}
