package ink.whi.web.admin.rest;

import ink.whi.api.model.enums.OperateArticleEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.web.admin.settings.ArticleSettings;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleSettingsService;
import ink.whi.web.admin.vo.ArticleListVo;
import ink.whi.web.base.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理后台
 *
 * @author qing
 * @date 2023/5/10
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "admin/article")
public class ArticleSettingRestController extends BaseRestController {

    @Autowired
    private ArticleSettingsService articleSettingService;

    /**
     * 编辑文章标题、发布状态等信息
     *
     * @param req
     * @return
     */
    @PostMapping(path = "save")
    @Permission(role = UserRole.ADMIN)
    public ResVo<String> save(@RequestBody ArticlePostReq req) {
        PushStatusEnum status = PushStatusEnum.formCode(req.getStatus());
        if (status == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "发布状态不合法!");
        }
        articleSettingService.updateArticle(req);
        return ResVo.ok("ok");
    }

    /**
     * 修改文章置顶、官方、加精等状态
     *
     * @param articleId
     * @param operateType 1-官方 2-非官方 3-置顶 4-取消置顶 5-推荐 6-不推荐
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "articleId") Long articleId, @RequestParam(name = "operateType") Integer operateType) {
        OperateArticleEnum operate = OperateArticleEnum.fromCode(operateType);
        if (operate == OperateArticleEnum.EMPTY) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, operateType + "非法");
        }
        articleSettingService.operateArticle(articleId, operate);
        return ResVo.ok("ok");
    }

    /**
     * 删除文章接口
     *
     * @param articleId
     * @return
     */
    @GetMapping(path = "delete")
    @Permission(role = UserRole.ADMIN)
    public ResVo<String> delete(@RequestParam(name = "articleId") Long articleId) {
        articleSettingService.deleteArticle(articleId);
        return ResVo.ok("ok");
    }

    /**
     * 文章列表接口
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping(path = "list")
    public ResVo<ArticleListVo> list(@RequestParam(name = "pageNumber", required = false) Long pageNumber, @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNumber, pageSize);
        ArticleListVo vo = new ArticleListVo();
        vo.setPage(articleSettingService.getArticleList(pageParam));
        vo.setReview(ArticleSettings.getReview());
        return ResVo.ok(vo);
    }
}
