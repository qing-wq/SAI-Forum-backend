package ink.whi;

import com.rabbitmq.client.BuiltinExchangeType;
import ink.whi.api.model.vo.notify.RabbitmqMsg;
import ink.whi.api.model.vo.notify.enums.NotifyTypeEnum;
import ink.whi.core.cache.RedisClient;
import ink.whi.core.common.RabbitmqConfig;
import ink.whi.core.utils.JsonUtil;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.user.repo.dao.UserFootDao;
import ink.whi.service.user.repo.entity.UserFootDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: qing
 * @Date: 2023/8/2
 */
public class RabbitmqTest extends BasicTest {

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private UserFootDao userFootDao;

    @Test
    public void getMessage() {
        UserFootDO userFoot = userFootDao.getById(2);
        RabbitmqMsg<UserFootDO> notify = new RabbitmqMsg<>(NotifyTypeEnum.PRAISE, userFoot);
        String str = JsonUtil.toStr(notify);
        String str1 = JsonUtil.toStr(userFoot);
        System.out.println(str);
        System.out.println(str1);
    }

    @Test
    public void testProductRabbitmq() {
        try {
            String message = "{\"notifyType\":\"PRAISE\",\"content\":{\"id\":2,\"createTime\":1681572523000,\"updateTime\":1682328515000,\"userId\":3,\"documentId\":101,\"documentType\":1,\"documentUserId\":1,\"collectionStat\":0,\"readStat\":1,\"commentStat\":0,\"praiseStat\":0}}\n";
            rabbitmqService.publishMsg(
                    RabbitmqConfig.EXCHANGE_NAME_DIRECT,
                    BuiltinExchangeType.DIRECT,
                    RabbitmqConfig.QUEUE_KEY_PRAISE,
                    message);
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
