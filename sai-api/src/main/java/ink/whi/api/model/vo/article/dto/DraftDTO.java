package ink.whi.api.model.vo.article.dto;

import ink.whi.api.model.vo.article.req.DraftSaveReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: qing
 * @Date: 2023/7/7
 */
@Data
public class DraftDTO implements Serializable{
    @Serial
    private static final long serialVersionUID = 5712100597276977003L;

    /**
     * 业务id
     */
    private Long draftId;

    /**
     * 作者id
     */
    private Long author;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
