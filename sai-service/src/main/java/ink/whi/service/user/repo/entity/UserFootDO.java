package ink.whi.service.user.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户足迹表
 *
 * @author qing
 * @date 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_foot")
public class UserFootDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 文档类型：1-文章，2-评论
     */
    private Integer documentType;
    /**
     * 发布该文档的用户ID
     */
    private Long documentUserId;
    /**
     * 收藏状态: 0-未收藏，1-已收藏
     */
    private Integer collectionStat;

    /**
     * 阅读状态: 0-未读，1-已读
     */
    private Integer readStat;

    /**
     * 评论状态: 0-未评论，1-已评论
     */
    private Integer commentStat;

    /**
     * 点赞状态: 0-未点赞，1-已点赞
     */
    private Integer praiseStat;
}
