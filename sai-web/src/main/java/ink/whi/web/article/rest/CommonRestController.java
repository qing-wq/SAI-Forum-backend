package ink.whi.web.article.rest;

import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.DraftsDTO;
import ink.whi.api.model.vo.article.req.DraftsSaveReq;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.DraftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章（草稿）通用接口
 *
 * @author: qing
 * @Date: 2023/10/3
 */
@RestController
@RequestMapping("common")
@Permission(role = UserRole.LOGIN)
public class CommonRestController {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private DraftsService draftsService;

    /**
     * 文章/草稿 编辑
     *
     * @param docId
     * @param type 0-草稿，1-文章
     * @return
     */
    @GetMapping(path = "edit/{docId}/{type}")
    public ResVo<DraftsDTO> edit(@PathVariable Long docId, @PathVariable Integer type) {
        ArticleTypeEnum articleType = ArticleTypeEnum.formCode(type);
        DraftsDTO dto = null;
        if (articleType == ArticleTypeEnum.BLOG) {
            dto = articleReadService.getOnlineArticleDraft(docId);
        } else {
            dto = articleReadService.queryDraftById(docId);
        }
        return ResVo.ok(dto);
    }

    /**
     * 自动保存草稿
     *
     * @param draftsSaveReq
     * @return
     */
    @PostMapping(path = "update")
    public ResVo<String> autoSave(@RequestBody DraftsSaveReq draftsSaveReq) {
        if (draftsSaveReq.getDraftId() == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "draftId=null");
        }
        draftsService.updateDraft(draftsSaveReq);
        return ResVo.ok("ok");
    }
}
