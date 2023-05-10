package ink.whi.web.base;

import ink.whi.api.model.vo.page.PageParam;

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
