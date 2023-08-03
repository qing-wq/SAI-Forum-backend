package ink.whi.service.notify.service.rabbitmq;

import com.rabbitmq.client.*;
import ink.whi.api.model.vo.notify.RabbitmqMsg;
import ink.whi.core.common.CommonConstants;
import ink.whi.core.rabbitmq.RabbitmqConnection;
import ink.whi.core.rabbitmq.RabbitmqConnectionPool;
import ink.whi.core.utils.JsonUtil;
import ink.whi.core.utils.SpringUtil;
import ink.whi.service.notify.service.RabbitmqService;
import ink.whi.service.notify.service.ProcessMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author: qing
 * @Date: 2023/7/31
 */
@Slf4j
@Service
public class RabbitmqServiceImpl implements RabbitmqService {

    @Autowired
    private ProcessMsgService processMsgService;

    @Override
    public boolean enabled() {
        return "true".equalsIgnoreCase(SpringUtil.getConfig("rabbitmq.switchFlag"));
    }

    @Override
    public void publishMsg(String exchange,
                           BuiltinExchangeType exchangeType,
                           String routingKey,
                           String message) {
        try {

            //创建连接
            RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
            Connection connection = rabbitmqConnection.getConnection();
            //创建消息通道
            Channel channel = connection.createChannel();
            // 声明exchange中的消息为可持久化，不自动删除
            channel.exchangeDeclare(exchange, exchangeType, true, false, null);
            // 发布消息
            channel.basicPublish(exchange, routingKey, MessageProperties.MINIMAL_BASIC, message.getBytes());
            log.info("Publish msg: {}", message);
            channel.close();
            RabbitmqConnectionPool.returnConnection(rabbitmqConnection);
        } catch (InterruptedException | IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void consumerMsg(String exchange,
                            String queueName,
                            String routingKey) {

        try {
            //创建连接
            RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
            Connection connection = rabbitmqConnection.getConnection();
            //创建消息信道
            final Channel channel = connection.createChannel();
            //消息队列
            channel.queueDeclare(queueName, true, false, false, null);
            //绑定队列到交换机
            channel.queueBind(queueName, exchange, routingKey);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    log.info("Consumer msg: {}", message);

                    // fixme: 用工厂 + 策略模式优化
                    processMsgService.processMsg(message, JsonUtil.toObj(message, RabbitmqMsg.class));
//                    consumerService.processMsg(JsonUtil.toObj(message, NotifyMsg.class));

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            // 取消自动ack
            channel.basicConsume(queueName, false, consumer);
            channel.close();
            RabbitmqConnectionPool.returnConnection(rabbitmqConnection);
        } catch (InterruptedException | IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processConsumerMsg() {
        log.info("Begin to processConsumerMsg.");

        Integer stepTotal = 1;
        Integer step = 0;

        // TODO: 改造成阻塞 I/O 模式
        while (true) {
            step++;
            try {
                log.info("processConsumerMsg cycle.");
                consumerMsg(CommonConstants.EXCHANGE_NAME_DIRECT, CommonConstants.QUEUE_NAME_PRAISE,
                        CommonConstants.QUEUE_KEY_PRAISE);
                if (step.equals(stepTotal)) {
                    Thread.sleep(10000);
                    step = 0;
                }
            } catch (Exception e) {

            }
        }
    }
}
