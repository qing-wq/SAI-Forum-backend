package ink.whi.web.article.rest;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import ink.whi.api.model.constants.RankConstants;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.dto.SimpleArticleDTO;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.req.ContentPostReq;
import ink.whi.api.model.vo.comment.dto.TopCommentDTO;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.user.dto.UserStatisticInfoDTO;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.rabbitmq.BlogMqConstants;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.ai.agent.AISearchHelper;
import ink.whi.service.ai.agent.AISummaryHelper;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.CategoryService;
import ink.whi.service.article.service.TagService;
import ink.whi.service.comment.service.CommentReadService;
import ink.whi.service.rank.impl.RankServiceImpl;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.CountService;
import ink.whi.service.user.service.UserFootService;
import ink.whi.service.user.service.UserService;
import ink.whi.web.article.vo.ArticleDetailVo;
import ink.whi.web.base.BaseRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文章查询接口
 *
 * @author: qing
 * @Date: 2023/4/28
 */
@Slf4j
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
    private CountService countService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AISummaryHelper summaryHelper;

    @Autowired
    private AISearchHelper searchHelper;

    @Autowired
    private ImageModel imageModel;
    
    @Autowired
    private RankServiceImpl rankService;

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

        // 如果是登录用户，更新用户活跃度分数
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        if (userId != null) {
            // 浏览文章加分
            rankService.incrementUserScore(userId, RankConstants.READ_SCORE);
        }

        ArticleDetailVo vo = new ArticleDetailVo();

        // 文章详情
        ArticleDTO article = articleReadService.queryTotalArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        article.setContent(article.getContent());
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
     * @param operateType 2-点赞 3-收藏 4-取消点赞 5-取消收藏
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

        // 1.更新数据库
        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(),
                ReqInfoContext.getReqInfo().getUserId(), type);

        // 消息通知
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(type);
        Optional.ofNullable(notifyType).ifPresent(s -> rabbitTemplate.convertAndSend(BlogMqConstants.BLOG_TOPIC_EXCHANGE,
                s == NotifyTypeEnum.PRAISE ? BlogMqConstants.BLOG_PRAISE_KEY : BlogMqConstants.BLOG_CANCEL_PRAISE_KEY, foot));
        return ResVo.ok(true);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @GetMapping(path = "category")
    public ResVo<List<CategoryDTO>> listCategory() {
        List<CategoryDTO> list = categoryService.loadAllCategories();
        return ResVo.ok(list);
    }

    /**
     * 查询所有标签
     *
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
     * 提取摘要(抽取前200个字)
     *
     * @param req
     * @return
     */
    @PostMapping(path = "generateSummary")
    public ResVo<String> generateSummary(@RequestBody ContentPostReq req) {
        return ResVo.ok(articleReadService.generateSummary(req.getContent()));
    }

    /**
     * AI生成摘要
     * @param req
     * @return
     */
    @PostMapping(path = "ai/summary")
    public ResVo<String> AISummary(@RequestBody ContentPostReq req) {
        String summary = summaryHelper.chat(req.getContent());
        return ResVo.ok(summary);
    }

    /**
     * AI图片生成
     * @param message
     * @return
     */
    @GetMapping(path = "ai/image")
    public ResVo<String> getPic(String message) {
        Response<Image> image = imageModel.generate("Donald Duck in New York, cartoon style");
        URI url = image.content().url();
        return ResVo.ok(url.toString());
    }

    /**
     * AI搜索
     * @param
     * @return
     */
    @PostMapping(path = "ai/search")
    public ResVo<List<SimpleArticleDTO>> aiSearch(@RequestBody String userMessage) {
        Map<String, String> params = searchHelper.chat(userMessage);
        List<SimpleArticleDTO> articles = articleReadService.queryArticleCluster(params);
        return ResVo.ok(articles);
    }
}
