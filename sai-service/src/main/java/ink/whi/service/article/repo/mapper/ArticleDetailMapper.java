package ink.whi.service.article.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Component
public interface ArticleDetailMapper extends BaseMapper<ArticleDetailDO> {
    @Update("update article_detail set content = #{content}, version = version + 1 where article_id = #{articleId} and `deleted` = 0 order by version desc limit 1")
    void updateContent(Long articleId, String content);
}
