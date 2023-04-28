package ink.whi.service.user.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.vo.user.dto.ArticleFootCountDTO;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.repo.mapper.UserFootMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Repository
public class UserFootDao extends ServiceImpl<UserFootMapper, UserFootDO> {

    /**
     * 查询作者全部文章计数信息
     * @param userId
     * @return
     */
    public ArticleFootCountDTO countArticleByUserId(Long userId) {
        ArticleFootCountDTO count = baseMapper.countArticleByUserId(userId);
        Optional.ofNullable(count).ifPresent(s -> s.setReadCount(baseMapper.countArticleReadsByUserId(userId)));
        return count;
    }

    public ArticleFootCountDTO countArticleByArticleId(Long articleId) {
        return baseMapper.countArticleByArticleId(articleId);
    }
}
