package ink.whi.service.article.service;

import ink.whi.api.model.vo.page.PageListVo;
import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.article.dto.TagDTO;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
public interface TagService {
    PageListVo<TagDTO> queryTagsList(PageParam pageParam);
}
