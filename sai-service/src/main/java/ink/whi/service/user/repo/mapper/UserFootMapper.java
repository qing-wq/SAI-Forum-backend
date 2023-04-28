package ink.whi.service.user.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.whi.api.model.vo.user.dto.ArticleFootCountDTO;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.repo.entity.UserFootDO;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public interface UserFootMapper extends BaseMapper<UserFootDO> {
    ArticleFootCountDTO countArticleByArticleId(Long articleId);

    Integer countArticleReadsByUserId(Long userId);

    ArticleFootCountDTO countArticleByUserId(Long userId);
}
