package ink.whi.service.article.service.Impl;

import ink.whi.api.model.enums.ArticleEventEnum;
import ink.whi.api.model.enums.OperateArticleEnum;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.api.model.vo.user.dto.BaseUserInfoDTO;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.ArticleDao;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.service.ArticleSettingsService;
import ink.whi.service.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 文章后台接口
 *
 * @author: qing
 * @Date: 2023/5/5
 */
@Service
public class ArticleSettingsServiceImpl implements ArticleSettingsService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private UserService userService;

    @Override
    public Integer getArticleCount() {
        return articleDao.countArticle();
    }

    @Override
    public void updateArticle(ArticlePostReq req) {
        ArticleDO article = articleDao.getById(req.getArticleId());
        if (article == null) {
            return;
        }

        if (StringUtils.isNotBlank(req.getTitle())) {
            article.setTitle(req.getTitle());
        }
        if (StringUtils.isNotBlank(req.getShortTitle())) {
            article.setShortTitle(req.getShortTitle());
        }

        ArticleEventEnum operateEvent = null;
        if (req.getStatus() != null) {
            article.setStatus(req.getStatus());
            if (req.getStatus() == PushStatusEnum.OFFLINE.getCode()) {
                operateEvent = ArticleEventEnum.OFFLINE;
            } else if (req.getStatus() == PushStatusEnum.REVIEW.getCode()) {
                operateEvent = ArticleEventEnum.REVIEW;
            } else if (req.getStatus() == PushStatusEnum.ONLINE.getCode()) {
                operateEvent = ArticleEventEnum.ONLINE;
            }
        }
        articleDao.updateById(article);

        // todo: 发布文章待审核、上线、下线事件
    }

    @Override
    public void operateArticle(Long articleId, OperateArticleEnum operate) {
        ArticleDO articleDO = articleDao.getById(articleId);
        if (articleDO == null || !setArticleStat(articleDO, operate)) {
            return;
        }
        articleDao.updateById(articleDO);
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO dto = articleDao.getById(articleId);
        if (dto == null || Objects.equals(dto.getDeleted(), YesOrNoEnum.YES.getCode())) {
            return;
        }
        dto.setDeleted(YesOrNoEnum.YES.getCode());
        articleDao.updateById(dto);
    }

    @Override
    public PageVo<ArticleDTO> getArticleList(PageParam pageParam) {
        List<ArticleDO> articleDOS = articleDao.listArticles(pageParam);
        List<ArticleDTO> articleDTOS = ArticleConverter.toArticleDtoList(articleDOS);
        articleDTOS.forEach(articleDTO -> {
            BaseUserInfoDTO user = userService.queryBasicUserInfo(articleDTO.getAuthor());
            articleDTO.setAuthorName(user.getUserName());
        });
        Integer totalCount = articleDao.countArticle();
        return PageVo.build(articleDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }

    private boolean setArticleStat(ArticleDO articleDO, OperateArticleEnum operate) {
        return switch (operate) {
            case OFFICIAL, CANCEL_OFFICIAL ->
                    compareAndUpdate(articleDO::getOfficialStat, articleDO::setOfficialStat, operate.getDbStatCode());
            case TOPPING, CANCEL_TOPPING ->
                    compareAndUpdate(articleDO::getToppingStat, articleDO::setToppingStat, operate.getDbStatCode());
            case CREAM, CANCEL_CREAM ->
                    compareAndUpdate(articleDO::getCreamStat, articleDO::setCreamStat, operate.getDbStatCode());
            default -> false;
        };
    }

    private <T> boolean compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return false;
        }
        consumer.accept(input);
        return true;
    }
}
