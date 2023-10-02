package ink.whi.api.model.vo.article.dto;

import ink.whi.api.model.enums.SourceTypeEnum;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */@Data
public class ArticleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -793906904770296838L;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章类型：1-博文，2-问答
     */
    private Integer articleType;

    /**
     * 作者uid
     */
    private Long author;

    /**
     * 作者名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 短标题(可选)
     */
    private String shortTitle;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 封面(可选)
     */
    private String cover;

    /**
     * 正文
     */
    private String content;

    /**
     * 文章来源(可选)
     *
     * @see SourceTypeEnum
     */
    private String sourceType;

    /**
     * 原文地址(可选)
     */
    private String sourceUrl;

    /**
     * 状态：0-未发布，1-已发布，2-待审核
     */
    private Integer status;

    /**
     * 是否官方
     */
    private Integer official;

    /**
     * 是否置顶
     */
    private Integer toppingStat;

    /**
     * 是否推荐
     */
    private Integer recommendStat;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后更新时间
     */
    private Long lastUpdateTime;

    /**
     * 分类
     */
    private CategoryDTO category;

    /**
     * 标签
     */
    private List<TagDTO> tags;

    /**
     * 表示当前查看的用户是否已经点赞过
     */
    private Boolean praised;

    /**
     * 表示当用户是否评论过
     */
    private Boolean commented;

    /**
     * 表示当前用户是否收藏过
     */
    private Boolean collected;

    /**
     * 文章对应的统计计数
     */
    private ArticleFootCountDTO count;

    /**
     * 点赞用户信息
     */
    private List<SimpleUserInfoDTO> praisedUsers;
}
