package ink.whi.service.article.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import ink.whi.api.model.enums.ColumnStatusEnum;
import ink.whi.api.model.enums.ColumnTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("column_info")
public class ColumnInfoDO extends BaseDO {

    private static final long serialVersionUID = 1920830534262012026L;
    /**
     * 专栏名
     */
    private String columnName;

    /**
     * 作者
     */
    private Long userId;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 封面
     */
    private String cover;

    /**
     * 状态
     *
     * @see ColumnStatusEnum#getCode()
     */
    private Integer state;

    /**
     * 排序
     */
    private Integer section;

    /**
     * 上线时间
     */
    private Date publishTime;

    /**
     * 专栏预计的文章数
     */
    private Integer nums;

    /**
     * 专栏类型：免费、登录阅读、收费阅读等
     * @see ColumnTypeEnum#getType()
     */
    private Integer type;

    /**
     * 免费开始时间
     */
    private Date freeStartTime;

    /**
     * 免费结束时间
     */
    private Date freeEndTime;
}
