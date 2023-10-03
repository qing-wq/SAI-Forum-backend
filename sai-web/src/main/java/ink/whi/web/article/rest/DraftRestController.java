package ink.whi.web.article.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.web.base.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 草稿相关
 * @author: qing
 * @Date: 2023/7/8
 */
@RestController
@RequestMapping(path = "draft")
public class DraftRestController extends BaseRestController {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private ArticleWriteService articleWriteService;

    /**
     * 获取用户草稿箱列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "list")
    public ResVo<PageListVo<ArticleDTO>> Drafts(@RequestParam(name = "page") Long pageNum,
                                              @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        PageListVo<ArticleDTO> drafts = articleReadService.listDraft(userId, pageParam);
        return ResVo.ok(drafts);
    }

    /**
     * 获取草稿详情
     * @param draftId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "draft/{draftId}")
    public ResVo<ArticleDTO> draft(@PathVariable Long draftId) {
        ArticleDTO dto = articleReadService.queryDraftById(draftId);
        return ResVo.ok(dto);
    }

    /**
     * 自动保存草稿
     * @param articlePostReq
     * @return
     */
    @PostMapping(path = "update")
    public ResVo<String> autoSave(@RequestBody ArticlePostReq articlePostReq) {
        articleWriteService.updateDraft(articlePostReq);
        return ResVo.ok("ok");
    }

    /**
     * 删除草稿
     * @param draftId
     * @return
     */
    @GetMapping(path = "del")
    public ResVo<String> del(Long draftId) {
        articleWriteService.deleteArticle(draftId);
        return ResVo.ok("ok");
    }
}
