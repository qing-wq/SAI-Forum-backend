package ink.whi.api.model.vo.statistic.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
@Data
@Builder
public class StatisticsCountDTO {

    @Tolerate
    public StatisticsCountDTO() {
    }

    /**
     * 统计计数
     */
    private Integer pvCount;

    /**
     * 总用户数
     */
    private Integer userCount;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 文章相关计数
     */
    private ArticleStatisticCountDTO articleStatisticCount;
}
