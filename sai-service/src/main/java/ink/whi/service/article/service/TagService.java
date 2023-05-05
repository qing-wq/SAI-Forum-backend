package ink.whi.service.article.service;

import ink.whi.api.model.vo.PageParam;
import ink.whi.api.model.vo.article.dto.TagDTO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
public interface TagService {
    List<TagDTO> queryTagsList(PageParam pageParam);
}
