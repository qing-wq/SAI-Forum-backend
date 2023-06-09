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
     * @param type 查看的消息类型，如：comment、reply、praise、collect、follow、system，默认comment
     * @return
     */
    @GetMapping(path = {"/", "/{type}"})
    public ResVo<NoticeResVo> list(@PathVariable(name = "type", required = false) String type) {
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        Map<String, Integer> map = notifyService.queryUnreadCounts(loginUserId);

        NotifyTypeEnum typeEnum = type == null ? null : NotifyTypeEnum.typeOf(type);
        if (typeEnum == null) {
            // 若没有指定查询的消息类别，则找一个存在消息未读数的进行展示
            typeEnum = map.entrySet().stream().filter(s -> s.getValue() > 0)
                    .map(s -> NotifyTypeEnum.typeOf(s.getKey()))
                    .findAny()
                    .orElse(NotifyTypeEnum.COMMENT);
        }

        NoticeResVo vo = new NoticeResVo();
        vo.setList(notifyService.queryUserNotices(loginUserId, typeEnum, PageParam.newPageInstance()));
        vo.setSelectType(typeEnum.name().toLowerCase());
        vo.setUnreadCountMap(map);
        return ResVo.ok(vo);
    }
}
