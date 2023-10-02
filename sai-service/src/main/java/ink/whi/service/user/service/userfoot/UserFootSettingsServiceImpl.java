package ink.whi.service.user.service.userfoot;

import ink.whi.api.model.vo.statistic.dto.ArticleStatisticCountDTO;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.service.UserFootSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: qing
 * @Date: 2023/10/2
 */
@Service
public class UserFootSettingsServiceImpl implements UserFootSettingsService {

    @Autowired
    private UserFootDao userFootDao;

    @Override
    public ArticleStatisticCountDTO getStatisticArticleTotalCount() {
        return userFootDao.getArticleTotalCount();
    }
}
