package ink.whi.service.article.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.YearArticleDTO;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ReadCountDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface ArticleMapper extends BaseMapper<ArticleDO> {
    List<YearArticleDTO> listYearArticleByUserId(Long userId);

    List<ReadCountDO> listArticleByCategoryAndTags(Long categoryId, List<Long> tagIds, PageParam pageParam);
}
