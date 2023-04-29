package ink.whi.web.article.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.article.MarkdownConverter;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.article.vo.ArticleDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;

import java.util.List;

/**
 * 文章查询接口
 * @author: qing
 * @Date: 2023/4/28
 */
@RestController
public class ArticleRestController {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentReadService commentReadService;

    /**
     * 文章详情接口
     * @param articleId
     * @return
     */
    @GetMapping(path = "detail/{articleId}")
    public ResVo<ArticleDetailVo> detail(@PathVariable Long articleId) {
        if (!NumUtil.upZero(articleId)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章ID不合法: " + articleId);
        }
        ArticleDetailVo vo = new ArticleDetailVo();

        // 文章详情
        ArticleDTO article = articleReadService.queryTotalArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        article.setContent(MarkdownConverter.markdownToHtml(article.getContent()));
        vo.setArticle(article);

        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(articleId, PageParam.newPageInstance());
        vo.setComments(comments);

        // 作者信息
        UserStatisticInfoDTO user = userService.queryUserInfoWithStatistic(article.getAuthor());
        article.setAuthorName(user.getUserName());
        article.setAuthorAvatar(user.getPhoto());
        vo.setAuthor(user);

        return ResVo.ok(vo);
    }
}
