package ink.whi.web.admin.rest;

import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.req.CategoryReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.service.CategorySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台分类管理接口
 * @author: qing
 * @Date: 2023/5/5
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "admin/category")
public class CategorySettingsRestController {

    @Autowired
    private CategorySettingService categorySettingService;

    /**
     * 添加分类
     * @param req
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody CategoryReq req) {
        categorySettingService.saveCategory(req);
        return ResVo.ok("ok");
    }

    /**
     * 删除分类
     * @param categoryId
     * @return
     */
    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "categoryId") Integer categoryId) {
        categorySettingService.deleteCategory(categoryId);
        return ResVo.ok("ok");
    }

    /**
     * 修改分类
     * @param categoryId
     * @param pushStatus
     * @return
     */
    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "categoryId") Integer categoryId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        categorySettingService.operateCategory(categoryId, pushStatus);
        return ResVo.ok("ok");
    }

    /**
     * 获取分类列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ResponseBody
    @GetMapping(path = "list")
    public ResVo<PageVo<CategoryDTO>> list(@RequestParam(name = "pageNumber", required = false) Long pageNumber,
                                           @RequestParam(name = "pageSize", required = false) Long pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<CategoryDTO> categoryDTOPageVo = categorySettingService.getCategoryList(PageParam.newPageInstance(pageNumber, pageSize));
        return ResVo.ok(categoryDTOPageVo);
    }
}
