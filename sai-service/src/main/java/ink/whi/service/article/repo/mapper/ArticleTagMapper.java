package ink.whi.service.article.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.repo.entity.ArticleTagDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {
    List<TagDTO> listArticleTagDetails(Long articleId);
}
