package ink.whi.service.user.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.article.dto.ArticleFootCountDTO;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.service.user.repo.entity.UserFootDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface UserFootMapper extends BaseMapper<UserFootDO> {
    ArticleFootCountDTO countArticleByArticleId(Long articleId);

    Integer countArticleReadsByUserId(Long userId);

    ArticleFootCountDTO countArticleByUserId(Long userId);

    List<SimpleUserInfoDTO> listPraiseUserByArticleId(Long articleId);
}
