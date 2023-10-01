package ink.whi.web.article.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.service.ArticleWriteService;
import ink.whi.service.article.service.DraftService;
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
     * 文章发布或更新
     * @param articlePostReq
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    public ResVo<Long> post(@RequestBody ArticlePostReq articlePostReq) {
        Long articleId = articleWriteService.saveArticle(articlePostReq);
        return ResVo.ok(articleId);
    }

    /**
     * 创建草稿
     * @param articlePostReq
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "init")
    public ResVo<Long> init(@RequestBody ArticlePostReq articlePostReq) {
        Long articleId = articleWriteService.initArticle(articlePostReq);
        return ResVo.ok(articleId);
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
