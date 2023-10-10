package ink.whi.api.model.vo.article.req;

import lombok.Data;

/**
 * @author: qing
 * @Date: 2023/7/22
 */
@Data
public class UserInfoSaveReq {

    /**
     * 用户ID (不需要填)
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String photo;

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

    /**
     * 个人简介
     */
    private String profile;
}
