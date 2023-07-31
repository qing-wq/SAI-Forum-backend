package ink.whi.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qing
 * @since 2023/7/31
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqProperties {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 路径
     */
    private String virtualhost;

    /**
     * 连接池大小
     */
    private Integer poolSize;

    /**
     * 是否开启
     */
    private Boolean switchFlag;
}
