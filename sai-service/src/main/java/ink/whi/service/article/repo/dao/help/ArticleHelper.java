package ink.whi.service.article.repo.dao.help;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.ArticleDetailDO;
import ink.whi.service.article.repo.mapper.ArticleDetailMapper;
import ink.whi.service.article.repo.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
@Component
public class ArticleHelper {

    /**
     * 查看审核内容鉴权
     * @param article
     * @return
     */
    public static boolean showReviewContent(ArticleDO article) {
        if (article.getStatus() != PushStatusEnum.REVIEW.getCode()) {
            return true;
        }

        BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        if (user == null) {
            return false;
        }

        // 作者本人和admin超管可以看到审核内容
        return user.getUserId().equals(article.getUserId()) || (user.getRole() != null && user.getRole().equalsIgnoreCase(UserRole.ADMIN.name()));
    }
}
