package ink.whi.web.Index;

import ink.whi.api.model.vo.ResVo;
import ink.whi.web.Index.helper.IndexRecommendHelper;
import ink.whi.web.Index.vo.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 首页接口
 *
 * @author: qing
 * @Date: 2023/4/27
 */
@Slf4j
@RestController
public class IndexHomeController {

    @Autowired
    private IndexRecommendHelper indexRecommendHelper;

    /**
     * 首页接口(目前暂时没被使用)
     *
     * @param request category-当前分类
     * @return
     */
    @GetMapping(path = {"/", "", "/index"})
    public ResVo<IndexVo> index(HttpServletRequest request) {
        String activeTab = request.getParameter("category");
        IndexVo vo = indexRecommendHelper.buildIndexVo(activeTab);
        // fixme: 增加全局参数msgCount
        return ResVo.ok(vo);
    }
}
