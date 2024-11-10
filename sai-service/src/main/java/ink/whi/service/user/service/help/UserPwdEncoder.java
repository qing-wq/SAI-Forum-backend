package ink.whi.service.user.service.help;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 密码加盐校验工具类
 * @author: qing
 * @Date: 2023/4/26
 */
@Component
public class UserPwdEncoder {
    /**
     * 盐值
     */
    @Value("${security.salt}")
    private String salt;

    /**
     * 加盐位置
     */
    @Value("${security.salt-index}")
    private Integer saltIndex;

    public String encoder(String pwd) {
        if (pwd.length() > saltIndex) {
            pwd = pwd.substring(0, saltIndex) + salt + pwd.substring(saltIndex);
        } else {
            pwd = pwd + salt;
        }
        return (DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8)));
    }

    public boolean match(String plainPwd, String encPwd) {
        if (plainPwd.length() > saltIndex) {
            plainPwd = plainPwd.substring(0, saltIndex) + salt + plainPwd.substring(saltIndex);
        } else {
            plainPwd = plainPwd + salt;
        }

        return Objects.equals(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)), encPwd);
    }

}
