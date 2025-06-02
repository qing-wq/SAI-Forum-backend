package ink.whi.web.rank.rest;

import ink.whi.api.model.vo.ResVo;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import ink.whi.service.rank.RankService;
import ink.whi.web.base.BaseRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 排行榜
 *
 * @author: qing
 * @Date: 2025/5/22
 */
@Slf4j
@RestController
@RequestMapping(path = "rank")
public class RankController extends BaseRestController {

    @Autowired
    private RankService rankService;

    /**
     * 用户活跃度排行榜
     */
    @GetMapping(path = "user")
    public ResVo<List<SimpleUserInfoDTO>> getUserRank(@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        if (limit <= 0 || limit > 100) {
            limit = 10;
        }
        List<SimpleUserInfoDTO> result = rankService.getUserRank(limit);
        return ResVo.ok(result);
    }
}
