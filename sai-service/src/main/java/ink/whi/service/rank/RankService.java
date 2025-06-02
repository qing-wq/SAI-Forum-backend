package ink.whi.service.rank;

import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;

import java.util.List;

/**
 * 活跃度排行榜
 *
 * @author: qing
 * @Date: 2025/5/22
 */
public interface RankService {

    void incrementUserScore(Long userId, double score);

    List<SimpleUserInfoDTO> getUserRank();

    List<SimpleUserInfoDTO> getUserRank(int limit);

    void refreshRankData();
}
