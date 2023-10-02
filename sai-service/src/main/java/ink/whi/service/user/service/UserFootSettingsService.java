package ink.whi.service.user.service;

import ink.whi.api.model.vo.statistic.dto.ArticleStatisticCountDTO;

/**
 * @author: qing
 * @Date: 2023/10/2
 */
public interface UserFootSettingsService {
    ArticleStatisticCountDTO getStatisticArticleTotalCount();
}
