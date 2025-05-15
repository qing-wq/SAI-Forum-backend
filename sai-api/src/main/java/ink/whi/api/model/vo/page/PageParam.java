package ink.whi.api.model.vo.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {

    public static final Long DEFAULT_PAGE_NUM = 1L;
    public static final Long DEFAULT_PAGE_SIZE = 10L;

    public static final Long TOP_PAGE_SIZE = 4L;


    /**
     * 请求页数，从1开始计数
     */
    private long pageNum;
    private long pageSize;
    private long offset;
    private long limit;

    public static PageParam newPageInstance() {
        return newPageInstance(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    public static PageParam newPageInstance(Integer pageNum, Integer pageSize) {
        return newPageInstance(pageNum.longValue(), pageSize.longValue());
    }

    public static PageParam newPageInstance(Long pageNum, Long pageSize) {
        if (pageNum <= 0) {
            pageNum = PageParam.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null || pageSize > PageParam.DEFAULT_PAGE_SIZE) {
            pageSize = PageParam.DEFAULT_PAGE_SIZE;
        }

        final PageParam pageParam = new PageParam();
        pageParam.pageNum = pageNum;
        pageParam.pageSize = pageSize;

        pageParam.offset = (pageNum - 1) * pageSize;
        pageParam.limit = pageSize;

        return pageParam;
    }

    public static String getLimitSql(PageParam pageParam) {
        if (pageParam == null) {
            return "";
        }
        return String.format("limit %s,%s", pageParam.offset, pageParam.limit);
    }
}
