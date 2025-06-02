package ink.whi.web.Index.vo;

import ink.whi.api.model.vo.article.dto.TagDTO;
import ink.whi.api.model.vo.user.dto.SimpleUserInfoDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author: qing
 * @Date: 2025/5/21
 */
@Data
public class ActiveDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8229869132569331218L;

    private List<TagDTO> activeArticle;
    private List<SimpleUserInfoDTO> activeUser;
}
