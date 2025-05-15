package ink.whi;

import ink.whi.api.model.enums.DocumentTypeEnum;
import ink.whi.core.rabbitmq.BlogMqConstants;
import ink.whi.service.user.repo.entity.UserFootDO;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: qing
 * @Date: 2023/8/2
 */
public class RabbitmqTest extends BasicTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate stringRedisTemplate;

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
            stringRedisTemplate.execute((RedisCallback<Void>) con -> {
                con.set("test".getBytes(), "123".getBytes());
                return null;
            });
            System.out.println(stringRedisTemplate.opsForValue().get("test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
