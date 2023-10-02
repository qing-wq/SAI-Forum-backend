package ink.whi.api.model.vo.statistic.dto;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/10/2
 */
@Data
public class ArticleStatisticCountDTO {
    /**
     * 收藏总数
     */
    private Integer collectCount;

    /**
     * 点赞总数
     */
    private Integer likeCount;

    /**
     * 阅读总数
     */
    private Integer readCount;

    /**
     * 评论总数
     */
    private Integer commentCount;

}
