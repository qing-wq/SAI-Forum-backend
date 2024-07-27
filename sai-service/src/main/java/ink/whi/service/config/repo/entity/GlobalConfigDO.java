package ink.whi.service.config.repo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author: qing
 * @Date: 2024/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("global_conf")
public class GlobalConfigDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = -6122208316544171301L;

    @TableField("`key`")
    private String key;

    private String value;

    private String comment;

    private Integer deleted;
}
