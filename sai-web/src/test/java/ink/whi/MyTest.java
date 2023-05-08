package ink.whi;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public class MyTest {
    @Test
    public void test() {
        String plainPwd = "123456";
        System.out.println(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)));
    }
}
