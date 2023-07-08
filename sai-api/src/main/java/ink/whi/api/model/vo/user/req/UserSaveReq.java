package ink.whi.api.model.vo.user.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @Date: 2023/7/7
 */
@Data
public class UserSaveReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 34489978092087873L;

    private Long userId;

    private String userName;

    private String password;

    private String studentId;

    private String college;

    private String email;
}
