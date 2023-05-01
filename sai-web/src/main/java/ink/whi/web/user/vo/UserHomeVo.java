package ink.whi.web.user.vo;

import ink.whi.api.model.enums.FollowSelectEnum;
import ink.whi.api.model.vo.PageListVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.TagSelectDTO;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author YiHui
 * @date 2022/9/2
 */
@Data
public class UserHomeVo {
    /**
     * 用户主页选择标签
     */
    private String homeSelectType;
    /**
     * 用户主页全部标签，如文章、浏览记录等
     */
    private List<TagSelectDTO> homeSelectTags;
    /**
     * 关注列表/粉丝列表
     */
    private PageListVo<FollowUserInfoDTO> followList;
    /**
     * 关注列表选项 follow-关注列表, fans-粉丝列表
     *
     * @see FollowSelectEnum#getCode()
     */
    private String followSelectType;
    /**
     * 关注者列表
     */
    private List<TagSelectDTO> followSelectTags;
    /**
     * 用户个人信息
     */
    private UserStatisticInfoDTO userHome;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> homeSelectList;
}
