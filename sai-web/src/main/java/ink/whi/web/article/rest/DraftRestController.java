package ink.whi.web.article.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.req.DraftsSaveReq;
import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.DraftsService;
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
    private DraftsService draftsService;

    /**
     * 获取用户草稿箱列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "list")
    public ResVo<PageListVo<DraftsDTO>> Drafts(@RequestParam(name = "page") Long pageNum,
                                              @RequestParam(name = "pageSize", required = false) Long pageSize) {
        PageParam pageParam = buildPageParam(pageNum, pageSize);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        PageListVo<DraftsDTO> drafts = draftsService.listDraft(userId, pageParam);
        return ResVo.ok(drafts);
    }

    /**
     * 创建草稿
     * @param draftsSaveReq
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "init")
    public ResVo<Long> init(@RequestBody DraftsSaveReq draftsSaveReq) {
        Long articleId = draftsService.initDraft(draftsSaveReq);
        return ResVo.ok(articleId);
    }

    /**
     * 删除草稿
     * @param draftId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "del/{draftId}")
    public ResVo<String> del(@PathVariable Long draftId) {
        draftsService.deleteDraft(draftId);
        return ResVo.ok("ok");
    }
}
