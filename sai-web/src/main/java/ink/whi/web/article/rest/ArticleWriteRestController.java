package ink.whi.web.article.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.DraftDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章增删改接口
 * @author: qing
 * @Date: 2023/4/28
 */
@RestController
@RequestMapping(path = "article/api")
public class ArticleWriteRestController {

    @Autowired
    private ArticleWriteService articleWriteService;

    /**
     * 文章发布或更新发布接口
     * @param articlePostReq
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    public ResVo<Long> post(@RequestBody ArticlePostReq articlePostReq) {
        Long articleId = articleWriteService.saveArticle(articlePostReq);
        // 删除文章草稿
        articleWriteService.deletedArticleDraft(articleId);
        return ResVo.ok(articleId);
    }

    /**
     * 存入草稿箱
     * @param draftSaveReq
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "save")
    public ResVo<Long> saveDraft(@RequestBody DraftSaveReq draftSaveReq) {
        Long draftId = articleWriteService.saveDraft(draftSaveReq);
        return ResVo.ok(draftId);
    }

    /**
     * 文章删除
     * @param articleId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "delete/{articleId}")
    public ResVo<String> delete(@PathVariable Long articleId) {
        articleWriteService.deleteArticle(articleId);
        return ResVo.ok("ok");
    }
}
