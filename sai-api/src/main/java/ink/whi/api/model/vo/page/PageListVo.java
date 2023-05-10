package ink.whi.api.model.vo.page;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 分页列表
 *
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class PageListVo<T> {

    /**
     * 分页列表
     */
    List<T> list;

    /**
     * 是否有更多
     */
    private Boolean hasMore;

    public static <T> PageListVo<T> emptyVo() {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(Collections.emptyList());
        vo.setHasMore(false);
        return vo;
    }

    public static <T> PageListVo<T> newVo(List<T> list, long pageSize) {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(Optional.ofNullable(list).orElse(Collections.emptyList()));
        // 根据pageSize判断是否查询完
        vo.setHasMore(vo.getList().size() == pageSize);
        return vo;
    }
}
