package ink.whi.service.statistics.repo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.whi.api.model.base.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.util.Date;

/**
 * @author qing
 * @date 2023/4/27
 */
@Builder
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("request_count")
public class RequestCountDO extends BaseDO {
    // fixme: 解决@data和@Builder导致无参构造消失的异常
    @Tolerate
    public RequestCountDO() {}

    @Serial
    private static final long serialVersionUID = 6586218844596585874L;

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
