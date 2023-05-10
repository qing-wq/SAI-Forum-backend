package ink.whi.service.article.service;

import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.article.req.TagReq;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.page.PageVo;

/**
 * @author: qing
 * @Date: 2023/5/10
 */
public interface TagSettingService {
    void saveTag(TagReq req);

    void deleteTag(Integer tagId);

    void operateTag(Integer tagId, Integer pushStatus);

    PageVo<TagDTO> getTagList(PageParam pageParam);
}
