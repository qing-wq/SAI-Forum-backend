package ink.whi;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.api.model.enums.OperateTypeEnum;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.cache.RedisClient;
import ink.whi.core.rabbitmq.BlogMqConstants;
import ink.whi.core.utils.JsonUtil;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.repo.entity.UserFootDO;
import ink.whi.service.user.service.UserFootService;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: qing
 * @Date: 2023/8/2
 */
public class RabbitmqTest extends BasicTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testProductRabbitmq() {
        try {
            String message = "{\"notifyType\":\"PRAISE\",\"content\":{\"id\":2,\"createTime\":1681572523000,\"updateTime\":1682328515000,\"userId\":3,\"documentId\":101,\"documentType\":1,\"documentUserId\":1,\"collectionStat\":0,\"readStat\":1,\"commentStat\":0,\"praiseStat\":0}}\n";
            Long articleId = 101L;
            Long author = 1L;
            Long userId = 3L;
            UserFootDO foot = new UserFootDO().setUserId(userId).setDocumentId(articleId).setDocumentType(DocumentTypeEnum.ARTICLE.getCode());
            System.out.println(foot);
            rabbitTemplate.convertAndSend(BlogMqConstants.BLOG_TOPIC_EXCHANGE, BlogMqConstants.BLOG_PRAISE_KEY, foot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRedis() {
        try {
            RedisClient.setStr("test", "aaa");
            System.out.println(RedisClient.getStr("test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
