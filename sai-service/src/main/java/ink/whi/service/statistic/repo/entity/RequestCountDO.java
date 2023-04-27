package ink.whi.service.statistic.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author qing
 * @date 2023/4/27
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName("request_count")
public class RequestCountDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 机器IP
     */
    private String host;

    /**
     * 访问计数
     */
    private Integer cnt;

    /**
     * 当前日期
     */
    private Date date;
}
