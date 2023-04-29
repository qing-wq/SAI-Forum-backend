package ink.whi.web.article.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.article.MarkdownConverter;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.article.vo.ArticleDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    private UserFootService userFootService;

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

    /**
     * 文章点赞、收藏相关操作
     * @param articleId
     * @param operateType
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public ResVo<Boolean> favor(@RequestParam(name = "articleId") Long articleId,
                                @RequestParam(name = "operate") Integer operateType) {
        OperateTypeEnum type = OperateTypeEnum.fromCode(operateType);
        if (type == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "参数非法：" + operateType);
        }

        ArticleDO article = articleReadService.queryBasicArticle(articleId);
        if (article == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在: " + articleId);
        }
        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(),
                ReqInfoContext.getReqInfo().getUserId(), type);

        // 消息通知

        return ResVo.ok(true);
    }
}
