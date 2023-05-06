package ink.whi.service.article.service.Impl;

import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.service.ArticleSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章后台接口
 * @author: qing
 * @Date: 2023/5/5
 */
@Service
public class ArticleSettingsServiceImpl implements ArticleSettingsService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Integer getArticleCount() {
        return articleDao.countArticle();
    }
}
