package ink.whi.web.admin.rest;

import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.req.TagReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.service.TagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签后台管理接口
 * @author: qing
 * @Date: 2023/5/10
 */
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping(path = "admin/tag")
public class TagSettingsRestController {
    @Autowired
    private TagSettingService tagSettingService;

    /**
     * 增加|修改标签
     * @param req
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody TagReq req) {
        tagSettingService.saveTag(req);
        return ResVo.ok("ok");
    }

    /**
     * 删除标签
     * @param tagId
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "tagId") Integer tagId) {
        tagSettingService.deleteTag(tagId);
        return ResVo.ok("ok");
    }

    /**
     * 修改标签状态
     * @param tagId
     * @param pushStatus 1-
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "tagId") Integer tagId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        tagSettingService.operateTag(tagId, pushStatus);
        return ResVo.ok("ok");
    }

    @GetMapping(path = "list")
    public ResVo<PageVo<TagDTO>> list(@RequestParam(name = "pageNumber", required = false) Long pageNumber,
                                      @RequestParam(name = "pageSize", required = false) Long pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<TagDTO> tagDTOPageVo = tagSettingService.getTagList(PageParam.newPageInstance(pageNumber, pageSize));
        return ResVo.ok(tagDTOPageVo);
    }
}
