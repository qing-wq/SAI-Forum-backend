package ink.whi.service.article.repo.dao.help;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.permission.UserRole;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/4/28
 */
public class ArticleHelper {

    /**
     * 查看内容鉴权
     * @param article
     * @return
     */
    public static boolean showContent(ArticleDO article) {
        // 只能查看已上线的文章
        if (article.getStatus() == PushStatusEnum.ONLINE.getCode()) {
            return true;
        }

        BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        if (user == null) {
            return false;
        }

        // 作者本人和超管可以看到审核内容
        return user.getUserId().equals(article.getUserId()) || (user.getRole() != null && user.getRole().equalsIgnoreCase(UserRole.ADMIN.name()));
    }

    public static boolean isOnline(ArticleDO article) {
        return article.getStatus() == PushStatusEnum.ONLINE.getCode();
    }
}
