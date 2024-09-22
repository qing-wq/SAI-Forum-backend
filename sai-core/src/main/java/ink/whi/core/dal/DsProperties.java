package ink.whi.core.dal;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 多数据源的配置加载
 *
 * @author qing
 * @date 2024/8/30
 */
@Data
@Component
@ConfigurationProperties(prefix = DsProperties.DS_PREFIX)
public class DsProperties {
    public static final String DS_PREFIX = "spring.dynamic";
    /**
     * 默认数据源
     */
    private String primary;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceProperties> datasource;
}