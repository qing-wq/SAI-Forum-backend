package ink.whi;

import ink.whi.service.user.service.help.UserPwdEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: qing
 * @Date: 2024/11/10
 */
public class PwdTest extends BasicTest{

    @Autowired
    public UserPwdEncoder userPwdEncoder;

    @Test
    public void test() {
        String plainPwd = "123456";
        System.out.println(userPwdEncoder.encoder(plainPwd));
    }
}
