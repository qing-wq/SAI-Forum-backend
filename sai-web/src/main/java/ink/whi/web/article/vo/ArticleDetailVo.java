package ink.whi.web.article.vo;

import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.api.model.vo.recommend.SideBarDTO;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Data
public class ArticleDetailVo {
    /**
     * 文章信息
     */
    private ArticleDTO article;

    /**
     * 评论信息
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论
     */
//    private TopCommentDTO hotComment;

    /**
     * 作者相关信息
     */
    private UserStatisticInfoDTO author;

    /**
     * 侧边栏信息
     */
//    private List<SideBarDTO> sideBarItems;

}