package ink.whi.web.base;

import ink.whi.api.model.vo.PageParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
public class BaseRestController {

    public PageParam buildPageParam(Long page, Long size) {
        if (page <= 0) {
            page = PageParam.DEFAULT_PAGE_NUM;
        }
        if (size == null || size > PageParam.DEFAULT_PAGE_SIZE) {
            size = PageParam.DEFAULT_PAGE_SIZE;
        }
        return PageParam.newPageInstance(page, size);
    }
}
