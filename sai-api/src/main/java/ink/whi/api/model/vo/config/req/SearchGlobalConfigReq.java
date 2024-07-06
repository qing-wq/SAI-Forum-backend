package ink.whi.api.model.vo.config.req;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
@Data
public class SearchGlobalConfigReq {
    private String keywords;
    private String value;
    private String comment;
    private Long pageNumber;
    private Long pageSize;
}
