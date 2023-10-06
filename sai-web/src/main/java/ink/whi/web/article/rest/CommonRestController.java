package ink.whi.web.article.rest;

import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.repo.dao.help.ArticleHelper;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import ink.whi.service.article.service.ArticleWriteService;
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
    private ArticleWriteService articleWriteService;

    /**
     * 文章/草稿 编辑
     *
     * @param articleId
     * @return
     */

    @GetMapping(path = "edit/{articleId}")
    public ResVo<ArticleDTO> edit(@PathVariable Long articleId) {
        ArticleDO article = articleReadService.queryBasicArticle(articleId);
        ArticleDTO dto = null;
        if (ArticleHelper.isOnline(article)) {
            dto = articleReadService.queryOnlineArticleCopy(articleId);
        } else {
            dto = articleReadService.queryDraftById(articleId);
        }
        return ResVo.ok(dto);
    }

    /**
     * 自动保存
     *
     * @param articlePostReq
     * @return
     */
    @PostMapping(path = "update")
    public ResVo<String> autoSave(@RequestBody ArticlePostReq articlePostReq) {
        Long articleId = articlePostReq.getArticleId();
        ArticleDO article = articleReadService.queryBasicArticle(articleId);
        if (article.getStatus() == PushStatusEnum.ONLINE.getCode()) {
            articleWriteService.updateArticle(articlePostReq);
        } else {
            articleWriteService.updateDraft(articlePostReq);
        }
        return ResVo.ok("ok");
    }
}
