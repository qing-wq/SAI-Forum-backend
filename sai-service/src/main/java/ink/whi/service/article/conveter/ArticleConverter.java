package ink.whi.service.article.conveter;

import ink.whi.api.model.enums.ArticleTypeEnum;
import ink.whi.api.model.enums.SourceTypeEnum;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.vo.article.dto.ArticleDTO;
import ink.whi.api.model.vo.article.dto.CategoryDTO;
import ink.whi.api.model.vo.article.dto.DraftDTO;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.api.model.vo.article.req.CategoryReq;
import ink.whi.api.model.vo.article.req.DraftSaveReq;
import ink.whi.api.model.vo.article.req.TagReq;
import ink.whi.service.article.repo.entity.ArticleDO;
import ink.whi.service.article.repo.entity.CategoryDO;
import ink.whi.service.article.repo.entity.DraftDO;
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

    public static ArticleDO toArticleDo(ArticlePostReq req, Long author) {
        ArticleDO article = new ArticleDO();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setShortTitle(req.getShortTitle());
        article.setArticleType(ArticleTypeEnum.valueOf(req.getArticleType().toUpperCase()).getCode());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setSource(req.getSource());
        article.setSourceUrl(req.getSourceUrl());
        article.setSummary(req.getSummary());
        article.setStatus(req.pushStatus().getCode());
        article.setDeleted(req.deleted() ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
        return article;
    }

    public static DraftDO toDraftDo(ArticlePostReq req, Long author) {
        DraftDO draft = new DraftDO();
        draft.setAuthor(author);
        draft.setArticleId(req.getArticleId());
        draft.setContent(req.getContent());
        draft.setTitle(req.getTitle());
        return draft;
    }

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
        articleDTO.setOfficial(articleDO.getOfficialStat());
        articleDTO.setToppingStat(articleDO.getToppingStat());
        articleDTO.setCreamStat(articleDO.getCreamStat());

        // 设置分类信息
        articleDTO.setCategory(new CategoryDTO(articleDO.getCategoryId(), null));
        return articleDTO;
    }

    public static List<ArticleDTO> toArticleDtoList(List<ArticleDO> articleDOS) {
        return articleDOS.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }

    /**
     * do转换
     *
     * @param tag
     * @return
     */
    public static TagDTO toDto(TagDO tag) {
        TagDTO dto = new TagDTO();
        dto.setTag(tag.getTagName());
        dto.setTagId(tag.getId());
        dto.setStatus(tag.getStatus());
        return dto;
    }

    public static List<TagDTO> toDtoList(List<TagDO> tags) {
        return tags.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }


    public static CategoryDTO toDto(CategoryDO category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setCategory(category.getCategoryName());
        dto.setCategoryId(category.getId());
        dto.setRank(category.getRank());
        dto.setStatus(category.getStatus());
        dto.setSelected(false);
        return dto;
    }

    public static List<CategoryDTO> toCategoryDtoList(List<CategoryDO> categorys) {
        return categorys.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }

    public static TagDO toDO(TagReq tagReq) {
        if (tagReq == null) {
            return null;
        }
        TagDO tagDO = new TagDO();
        tagDO.setTagName(tagReq.getTag());
        return tagDO;
    }

    public static CategoryDO toDO(CategoryReq categoryReq) {
        if (categoryReq == null) {
            return null;
        }
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setCategoryName(categoryReq.getCategory());
        categoryDO.setRank(categoryReq.getRank());
        return categoryDO;
    }

    public static DraftDTO toDto(DraftDO draft) {
        if (draft == null) {
            return null;
        }
        DraftDTO dto = new DraftDTO();
        dto.setDraftId(draft.getId());
        dto.setTitle(draft.getTitle());
        dto.setContent(draft.getContent());
        dto.setAuthor(draft.getAuthor());
        dto.setCreateTime(draft.getCreateTime());
        dto.setUpdateTime(draft.getUpdateTime());
        return dto;
    }

    public static List<DraftDTO> toDraftList(List<DraftDO> drafts) {
        return drafts.stream().map(ArticleConverter::toDto).toList();
    }
}
