package ink.whi.service.statistics.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author: qing
 * @Date: 2023/10/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dict_common")
public class DictCommonDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型
     */
    private String typeCode;

    /**
     * 字典类型的值编码
     */
    private String dictCode;

    /**
     * 字典类型的值描述
     */
    private String dictDesc;

    /**
     * 排序编号
     */
    private Integer sortNo;

    /**
     * 备注
     */
    private String remark;
}