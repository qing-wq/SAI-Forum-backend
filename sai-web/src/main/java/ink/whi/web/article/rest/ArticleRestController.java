package ink.whi.web.article.rest;

import com.rabbitmq.client.BuiltinExchangeType;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.req.ContentPostReq;
import ink.whi.api.model.vo.notify.RabbitmqMsg;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.article.MarkdownConverter;
import ink.whi.core.common.CommonConstants;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.JsonUtil;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.article.service.TagService;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.article.vo.ArticleDetailVo;
import ink.whi.web.base.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 文章查询接口
 *
 * @author: qing
 * @Date: 2023/4/28
 */
@RestController
@RequestMapping(path = "article")
public class ArticleRestController extends BaseRestController {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RabbitmqService rabbitmqService;

    /**
     * 文章详情接口
     *
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
     *
     * @param articleId
     * @param operateType
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public ResVo<Boolean> favor(@RequestParam(name = "articleId") Long articleId,
                                @RequestParam(name = "operate") Integer operateType) throws IOException, TimeoutException {
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
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(type);
//        Optional.ofNullable(notifyType).ifPresent(s -> SpringUtil.publishEvent(new NotifyMsgEvent<UserFootDO>(this, s, foot)));

        // 使用rabbitmq做消息通知
        rabbitmqService.publishMsg(
                CommonConstants.EXCHANGE_NAME_DIRECT,
                BuiltinExchangeType.DIRECT,
                CommonConstants.QUEUE_KEY_PRAISE,
                JsonUtil.toStr(new RabbitmqMsg<>(notifyType, foot)));
        return ResVo.ok(true);
    }

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping(path = "category")
    public ResVo<List<CategoryDTO>> listCategory() {
        List<CategoryDTO> list = categoryService.loadAllCategories();
        return ResVo.ok(list);
    }

    /**
     * 查询所有标签
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(path = "tags")
    public ResVo<PageListVo<TagDTO>> listTags(@RequestParam(name = "page") Long pageNum,
                                              @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        PageListVo<TagDTO> list = tagService.queryTagsList(pageParam);
        return ResVo.ok(list);
    }

    /**
     * 提取摘要
     * @param req
     * @return
     */
    @PostMapping(path = "generateSummary")
    public ResVo<String> generateSummary(@RequestBody ContentPostReq req) {
        return ResVo.ok(articleReadService.generateSummary(req.getContent()));
    }

    /**
     * 已上线文章编辑
     * @param articleId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "article/{articleId}")
    public ResVo<ArticleDTO> edit(@PathVariable Long articleId) {
        ArticleDTO dto = articleReadService.queryArticleDraft(articleId);
        return ResVo.ok(dto);
    }
}
