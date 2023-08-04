package ink.whi.web.config;

//import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import ink.whi.core.config.RabbitmqProperties;
import ink.whi.core.rabbitmq.RabbitmqConnectionPool;
import ink.whi.service.notify.service.RabbitmqService;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * @author qing
 * @date 2023/7/31
 */
@Configuration
//@ConditionalOnProperty(prefix = "rabbitmq.enable", value = "true")
@EnableConfigurationProperties(RabbitmqProperties.class)
public class RabbitMqAutoConfig implements ApplicationRunner {

    @Autowired
    @Qualifier(value = "taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private  RabbitmqProperties rabbitmqProperties;

    /**
     * 初始化RabbitMq
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String host = rabbitmqProperties.getHost();
        Integer port = rabbitmqProperties.getPort();
        String userName = rabbitmqProperties.getUsername();
        String password = rabbitmqProperties.getPassword();
        String virtualhost = rabbitmqProperties.getVirtualhost();
        Integer poolSize = rabbitmqProperties.getPoolSize();
        RabbitmqConnectionPool.initRabbitmqConnectionPool(host, port, userName, password, virtualhost, poolSize);
//        taskExecutor.execute(() -> rabbitmqService.processConsumerMsg());
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory  connectionFactory =  new CachingConnectionFactory();
        connectionFactory.setUri("amqp://admin:admin@127.0.0.1:5672");
        return connectionFactory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
