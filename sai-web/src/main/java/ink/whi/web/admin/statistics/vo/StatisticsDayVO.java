package ink.whi.web.admin.statistics.vo;

import lombok.Data;

/**
 * 每日计数
 * @author: qing
 * @Date: 2023/5/6
 */
@Data
public class StatisticsDayVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;
}
