package ink.whi.api.model.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/4/25 23:14
 */
@Data
public class BaseDO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1706108459962747001L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;
}
