package ink.whi.web.notice.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.notify.service.NotifyMsgService;
import ink.whi.web.notice.vo.NoticeResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息接口
 * @author: qing
 * @Date: 2023/5/5
 */
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping(path = "notice")
public class NoticeRestController {

    @Autowired
    private NotifyMsgService notifyService;

    /**
     * 消息列表
     * @param type 消息类型，如：comment、reply、praise、collect、follow、system
     * @return
     */
    @GetMapping(path = {"/", "/{type}"})
    public ResVo<NoticeResVo> list(@PathVariable(name = "type", required = false) String type) {
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        Map<String, Integer> map = notifyService.queryUnreadCounts(loginUserId);

        NotifyTypeEnum typeEnum = type == null ? NotifyTypeEnum.COMMENT : NotifyTypeEnum.typeOf(type);

        NoticeResVo vo = new NoticeResVo();
        vo.setList(notifyService.queryUserNotices(loginUserId, typeEnum, PageParam.newPageInstance()));
        vo.setSelectType(typeEnum.name().toLowerCase());
        vo.setUnreadCountMap(map);
        return ResVo.ok(vo);
    }
}
