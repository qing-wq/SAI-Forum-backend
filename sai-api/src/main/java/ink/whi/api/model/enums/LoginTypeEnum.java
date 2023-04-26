package ink.whi.api.model.enums;

import lombok.Getter;

@Getter
public enum LoginTypeEnum {
    WX_LOGIN(0, "微信登录"),
    PASSWORD_LOGIN(1, "账号密码登录");

    private int code;
    private String type;

    LoginTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }
}
