package ink.whi.web.admin.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.statistic.dto.StatisticsCountDTO;
import ink.whi.api.model.vo.statistic.dto.StatisticsDayDTO;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.service.statistics.service.StatisticsSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台统计分析接口
 * @author: qing
 * @Date: 2023/5/5
 */
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping(path = "admin/statistics/")
public class StatisticsSettingsRestController {
    private static final Integer DEFAULT_DAY = 7;

    @Autowired
    private StatisticsSettingService statisticsSettingService;

    /**
     * 获取首页文章、用户、pv等统计信息
     * @return
     */
    @GetMapping(path = "queryTotal")
    public ResVo<StatisticsCountDTO> queryTotal() {
        StatisticsCountDTO statisticsCountDTO = statisticsSettingService.getStatisticsCount();
        return ResVo.ok(statisticsCountDTO);
    }

    /**
     * 获取pv,uv值列表
     * @param day 天数 默认 7
     * @return
     */
    @GetMapping(path = "dayList")
    public ResVo<List<StatisticsDayDTO>> pvAndUvDayList(@RequestParam(name = "day", required = false) Integer day) {
        day = (day == null || day == 0) ? DEFAULT_DAY : day;
        List<StatisticsDayDTO> pvDayList = statisticsSettingService.getPvAndUvDayList(day);
        return ResVo.ok(pvDayList);
    }

    /**
     * 获取uv值列表
     * @param day 天数 默认 7
     * @return
     */
//    @GetMapping(path = "uvDayList")
    public ResVo<List<StatisticsDayDTO>> uvDayList(@RequestParam(name = "day", required = false) Integer day) {
        day = (day == null || day == 0) ? DEFAULT_DAY : day;
        List<StatisticsDayDTO> pvDayList = statisticsSettingService.getUvDayList(day);
        return ResVo.ok(pvDayList);
    }
}
