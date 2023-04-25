package ink.whi.api.model.base;

import lombok.Data;

import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/4/25 23:19
 */
@Data
public class BaseDTO {
    private Long id;

    private Date createTime;

    private Date updateTime;
}
