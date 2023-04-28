package ink.whi.web.home;

import ink.whi.api.model.vo.ResVo;
import ink.whi.web.home.helper.IndexRecommendHelper;
import ink.whi.web.home.vo.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
     * 首页接口
     *
     * @param request category-当前分类
     * @return
     */
    @GetMapping(path = {"/", "", "/index"})
    public ResVo<IndexVo> index(HttpServletRequest request) {
        String activeTab = request.getParameter("category");
        IndexVo vo = indexRecommendHelper.buildIndexVo(activeTab);
        return ResVo.ok(vo);
    }
}
