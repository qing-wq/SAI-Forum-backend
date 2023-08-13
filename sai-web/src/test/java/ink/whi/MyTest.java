package ink.whi;

import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.vo.article.req.ArticlePostReq;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.web.SaiForumApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
@Slf4j
@SpringBootTest(classes = SaiForumApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MyTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        String plainPwd = "123456";
        System.out.println(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testProductRabbitmq() {
        try {
            String message = "{\"notifyType\":\"PRAISE\",\"content\":{\"id\":2,\"createTime\":1681572523000,\"updateTime\":1682328515000,\"userId\":3,\"documentId\":101,\"documentType\":1,\"documentUserId\":1,\"collectionStat\":0,\"readStat\":1,\"commentStat\":0,\"praiseStat\":0}}\n";
            Long articleId = 101L;
            Long author = 1L;
            Long userId = 3L;
            UserFootDO foot = new UserFootDO().setUserId(userId).setDocumentId(articleId).setDocumentType(DocumentTypeEnum.ARTICLE.getCode());
            System.out.println(foot);
            rabbitTemplate.convertAndSend("topic", "aaa", foot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modify(ArticlePostReq name1) {
        name1.setContent("aaaaaaa");
    }
}
