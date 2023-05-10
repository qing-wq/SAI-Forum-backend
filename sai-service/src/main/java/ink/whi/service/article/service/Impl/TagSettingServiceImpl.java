package ink.whi.service.article.service.Impl;

import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.req.TagReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;
import ink.whi.core.utils.NumUtil;
import ink.whi.service.article.conveter.ArticleConverter;
import ink.whi.service.article.repo.dao.TagDao;
import ink.whi.service.article.repo.entity.TagDO;
import ink.whi.service.article.service.TagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/10
 */
@Service
public class TagSettingServiceImpl implements TagSettingService {

    @Autowired
    private TagDao tagDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTag(TagReq tagReq) {
        TagDO tagDO = ArticleConverter.toDO(tagReq);

        if (NumUtil.nullOrZero(tagReq.getTagId())) {
            tagDao.save(tagDO);
        } else {
            tagDO.setId(tagReq.getTagId());
            tagDao.updateById(tagDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Integer tagId) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){
            tagDao.removeById(tagId);
        }
    }

    @Override
    public void operateTag(Integer tagId, Integer pushStatus) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){
            tagDO.setStatus(pushStatus);
            tagDao.updateById(tagDO);
        }
    }

    @Override
    public PageVo<TagDTO> getTagList(PageParam pageParam) {
        List<TagDTO> tagDTOS = tagDao.listTag(pageParam);
        Integer totalCount = tagDao.countTag();
        return PageVo.build(tagDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}
