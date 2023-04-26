package ink.whi.web.test;

import com.mysql.cj.log.Log;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.ForumException;
import ink.whi.api.model.vo.ResVo;
import ink.whi.web.base.BaseRestController;
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

    @GetMapping(path = "/error")
    public ResVo<String> test() {
        log.info("enter");
        throw ForumException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS);
    }
}
