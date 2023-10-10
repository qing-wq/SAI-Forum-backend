package ink.whi.api.model.exception;

import lombok.Getter;

/**
 * 异常码规范：业务 - 状态 - code
 *
 * - 100 通用
 * - 200 文章相关
 * - 300 评论相关
 * - 400 用户相关
 *
 * @author qing
 * @date 2022/4/26
 */
@Getter
public enum StatusEnum {
    SUCCESS(200, "OK"),

    // 全局传参异常
    ILLEGAL_ARGUMENTS(100_400_001, "参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(100_400_002, "参数异常:%s"),
    ILLEGAL_OPERATE(100_400_002, "操作非法:%s"),

    // 全局权限相关
    FORBID_ERROR(100_403_001, "无权限"),

    FORBID_ERROR_MIXED(100_403_002, "无权限:%s"),

    // 全局，数据不存在
    RECORDS_NOT_EXISTS(100_404_001, "记录不存在:%s"),

    // 系统异常
    UNEXPECT_ERROR(100_500_001, "非预期异常:%s"),

    // 图片相关异常类型
    UPLOAD_PIC_FAILED(100_500_002, "图片上传失败！"),

    ARTICLE_NOT_EXISTS(200_404_001, "文章不存在:%s"),

    // 评论相关异常类型
    COMMENT_NOT_EXISTS(300_404_001, "评论不存在:%s"),

    // 用户相关异常
    LOGIN_FAILED_MIXED(400_403_001, "登录失败:%s"),
    USER_NOT_EXISTS(400_404_001, "用户不存在:%s"),
    USER_PWD_ERROR(400_403_002, "用户名or密码错误"),

    // token异常
    JWT_VERIFY_EXISTS(500_400_001, "token校验异常"),
    TOKEN_NOT_EXISTS(500_403_002, "token不存在"),
    TOKEN_CREATE_EXISTS(500_500_003, "token创建异常")
    ;

    private int code;

    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean is5xx(int code) {
        return code % 1000_000 / 1000 >= 500;
    }

    public static boolean is403(int code) {
        return code % 1000_000 / 1000 == 403;
    }

    public static boolean is4xx(int code) {
        return code % 1000_000 / 1000 < 500;
    }
}
