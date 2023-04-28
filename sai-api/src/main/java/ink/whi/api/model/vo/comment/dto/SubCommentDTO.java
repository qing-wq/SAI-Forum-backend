package ink.whi.api.model.vo.comment.dto;

import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@ToString(callSuper = true)
@Data
public class SubCommentDTO extends BaseCommentDTO {

    /**
     * 父评论内容
     */
    private String parentContent;


    @Override
    public int compareTo(@NotNull BaseCommentDTO o) {
        return Long.compare(this.getCommentTime(), o.getCommentTime());
    }
}
