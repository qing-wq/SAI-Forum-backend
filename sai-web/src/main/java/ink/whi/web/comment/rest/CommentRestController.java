package ink.whi.web.comment.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.comment.CommentSaveReq;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.JsonUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.comment.repo.entity.CommentDO;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.comment.service.CommentWriteService;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.UserFootService;
import ink.whi.web.article.vo.ArticleDetailVo;
import ink.whi.web.base.BaseRestController;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 评论接口
 *
 * @author: qing
 * @Date: 2023/5/2
 */
@RestController
@RequestMapping(path = "comment/api")
public class CommentRestController extends BaseRestController {

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private CommentWriteService commentWriteService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private RabbitmqService rabbitmqService;

    /**
     * 评论列表分页接口
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(path = "list")
    public ResVo<PageListVo<TopCommentDTO>> list(@RequestParam(name = "articleId") Long articleId,
                                                 @RequestParam(name = "page") Long pageNum,
                                                 @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        List<TopCommentDTO> list = commentReadService.getArticleComments(articleId, pageParam);
        PageListVo<TopCommentDTO> vo = PageListVo.newVo(list, pageSize);
        return ResVo.ok(vo);
    }

    /**
     * 发布评论
     * @param req
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    public ResVo<ArticleDetailVo> save(@RequestBody CommentSaveReq req) {
        if (req.getArticleId() == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        req.setUserId(ReqInfoContext.getReqInfo().getUserId());
        req.setCommentContent(StringEscapeUtils.escapeHtml3(req.getCommentContent()));  // 替换html中特殊符号
        commentWriteService.saveComment(req);

        // 重新构建文章详情页
        ArticleDetailVo vo = new ArticleDetailVo();
        ArticleDO article = articleReadService.queryBasicArticle(req.getArticleId());
        vo.setArticle(ArticleConverter.toDto(article));
        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(req.getArticleId(), PageParam.newPageInstance());
        vo.setComments(comments);
        return ResVo.ok(vo);
    }

    /**
     * 评论点赞、取消点赞等操作
     * @param commentId
     * @param operateType
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public ResVo<String> favor(@RequestParam(name = "commentId") Long commentId,
                               @RequestParam(name = "type") Integer operateType) {
        CommentDO comment = commentReadService.queryComment(commentId);
        if (comment == null) {
            return ResVo.fail(StatusEnum.COMMENT_NOT_EXISTS, "评论不存在：" + commentId);
        }

        OperateTypeEnum type = OperateTypeEnum.fromCode(operateType);
        if (type != OperateTypeEnum.PRAISE && type != OperateTypeEnum.CANCEL_PRAISE) {
            // 评论只能进行点赞操作
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "操作非法: " + operateType);
        }
        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT, commentId, comment.getUserId(), ReqInfoContext.getReqInfo().getUserId(), type);
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(type);
        Optional.ofNullable(notifyType).ifPresent(s -> rabbitmqService.publishMsg(JsonUtil.toStr(new RabbitmqMsg<>(s, foot))));
        return ResVo.ok("ok");
    }
}
