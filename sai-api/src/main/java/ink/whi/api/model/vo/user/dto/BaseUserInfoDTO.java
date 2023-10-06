package ink.whi.api.model.vo.user.dto;

import ink.whi.api.model.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: qing
 * @Date: 2023/4/26
 */
@Data
@Accessors(chain = true)
public class BaseUserInfoDTO extends BaseDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色 必须是String
     */
    private String role;

    /**
     * 用户图像
     */
    private String photo;

    /**
     * 个人简介
     */
    private String profile;

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
     * 用户最后登录区域
     */
    private String region;
}
