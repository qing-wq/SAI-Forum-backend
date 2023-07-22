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
    private String userName;

    /**
     * 用户图像
     */
    private String photo;

    /**
     * 职位
     */
    private String position;

    /**
     * 公司
     */
    private String company;

    /**
     * 个人简介
     */
    private String profile;
}
