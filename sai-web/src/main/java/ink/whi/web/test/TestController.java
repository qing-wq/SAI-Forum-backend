package ink.whi.web.test;

import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.vo.ResVo;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: qing
 * @Date: 2023/4/26 11:22
 */
@Slf4j
@RestController
public class TestController {

    @Permission(role = UserRole.LOGIN)
//    @GetMapping(path = "/error")
    public ResVo<String> test() {
//        throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS);
        return ResVo.ok("ok");
    }
}
