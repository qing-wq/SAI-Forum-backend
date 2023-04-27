package ink.whi.service.article.conveter;

import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.enums.SourceTypeEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.rep.ArticlePostReq;
import ink.whi.api.model.vo.article.rep.CategoryReq;
import ink.whi.api.model.vo.article.rep.TagReq;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.CategoryDO;
import ink.whi.service.article.repo.entity.TagDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章转换器
 *
 * @author qing
 * @date 2023/4/27
 */
public class ArticleConverter {

    public static ArticleDTO toDto(ArticleDO articleDO) {
        if (articleDO == null) {
            return null;
        }
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setAuthor(articleDO.getUserId());
        articleDTO.setArticleId(articleDO.getId());
        articleDTO.setArticleType(articleDO.getArticleType());
        articleDTO.setTitle(articleDO.getTitle());
        articleDTO.setShortTitle(articleDO.getShortTitle());
        articleDTO.setSummary(articleDO.getSummary());
        articleDTO.setCover(articleDO.getPicture());
        articleDTO.setSourceType(SourceTypeEnum.formCode(articleDO.getSource()).getDesc());
        articleDTO.setSourceUrl(articleDO.getSourceUrl());
        articleDTO.setStatus(articleDO.getStatus());
        articleDTO.setCreateTime(articleDO.getCreateTime().getTime());
        articleDTO.setLastUpdateTime(articleDO.getUpdateTime().getTime());
        articleDTO.setOfficalStat(articleDO.getOfficialStat());
        articleDTO.setToppingStat(articleDO.getToppingStat());
        articleDTO.setCreamStat(articleDO.getCreamStat());

        // 设置类目id
        articleDTO.setCategory(new CategoryDTO(articleDO.getCategoryId(), null));
        return articleDTO;
    }


}
