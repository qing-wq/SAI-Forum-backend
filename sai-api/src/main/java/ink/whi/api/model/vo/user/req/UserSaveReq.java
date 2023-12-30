package ink.whi.api.model.vo.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
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

    /**
     * 用户名
     */
    @NotNull
    private String username;

    /**
     * 验证码
     */
    @NotNull
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 学院
     */
    private String college;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 专业
     */
    private String major;
}
