package ink.whi;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

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

    @Test
    public void test3() {
        Integer i1 = 40;
        Integer i2 = new Integer(40);
        System.out.println(i1==i2);
    }


    @Test
    public void test2() {
    }

    @Test
    public void modify() {
        System.out.println(LocalDate.now());
    }
}
