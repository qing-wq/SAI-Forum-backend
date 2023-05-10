package ink.whi;

import ink.whi.api.model.vo.article.req.ArticlePostReq;
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

    @Test
    public void test2() {
        ArticlePostReq req = new ArticlePostReq();
        req.setArticleId(123L);
        modify(req);
        System.out.println(req.getContent());
    }

    private static void modify(ArticlePostReq name1) {
        name1.setContent("aaaaaaa");
    }
}
