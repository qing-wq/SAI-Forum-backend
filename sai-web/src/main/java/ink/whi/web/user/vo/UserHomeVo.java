package ink.whi.web.user.vo;

import ink.whi.api.model.enums.FollowSelectEnum;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.TagSelectDTO;
import ink.whi.api.model.vo.user.dto.FollowUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserHomeVo {
    /**
     * 用户主页选择标签
     */
    private String homeSelectType;

    /**
     * 用户关注列表选择标签 follow-关注列表, fans-粉丝列表
     *
     * @see FollowSelectEnum#getCode()
     */
    private String followSelectType;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> homeSelectList;

    /**
     * 关注列表/粉丝列表
     */
    private PageListVo<FollowUserInfoDTO> followList;

    /**
     * 用户个人信息
     */
    private UserStatisticInfoDTO userHome;
}
