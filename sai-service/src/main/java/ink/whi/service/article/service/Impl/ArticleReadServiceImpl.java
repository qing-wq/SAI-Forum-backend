package ink.whi.service.article.service.Impl;

import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.service.ArticleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Service
public class ArticleReadServiceImpl implements ArticleReadService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam pageParam) {
        List<ArticleDTO> list = articleDao.listArticleByCategoryId(categoryId, pageParam);
        return PageListVo.newVo(list, pageParam.getPageSize());
    }
}
