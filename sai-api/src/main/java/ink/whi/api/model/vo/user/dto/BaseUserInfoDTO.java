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
     * 用户角色
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
     * 职位
     */
    private String position;

    /**
     * 公司
     */
    private String company;

    /**
     * 扩展字段
     */
    private String extend;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 用户最后登录区域
     */
    private String region;
}
