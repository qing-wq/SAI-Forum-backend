package ink.whi.service.article.service.Impl;

import ink.whi.api.model.vo.page.PageParam;
import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.service.article.repo.dao.TagDao;
import ink.whi.service.article.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/5
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public List<TagDTO> queryTagsList(PageParam pageParam) {
        return tagDao.listTags(pageParam);
    }
}
