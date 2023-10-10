package ink.whi.api.model.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/4/25 23:19
 */
@Data
public class BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1314162575898615006L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
