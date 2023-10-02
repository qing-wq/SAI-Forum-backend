package ink.whi.api.model.vo.statistic.dto;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Data
public class StatisticsDayDTO {

    /**
     * 日期
     */
    private String date;

    /**
     * pv
     */
    private Integer pv;

    /**
     * uv
     */
    private Integer uv;
}

