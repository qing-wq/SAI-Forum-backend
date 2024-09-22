package ink.whi.core.dal;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 当配置了多数据源时，启用
 *
 * @author qing
 * @date 2024/8/30
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.dynamic", name = "primary")
@EnableConfigurationProperties(DsProperties.class)
public class DataSourceConfig {

    private final Environment environment;

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
        log.info("动态数据源初始化!");
    }

    @Bean
    public DsAspect dsAspect() {
        return new DsAspect();
    }

    /**
     * 整合主从数据源
     *
     * @param dsProperties
     * @return 1
     */
    @Bean
    @Primary
    public DataSource dataSource(DsProperties dsProperties) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(dsProperties.getDatasource().size());
        dsProperties.getDatasource().forEach((k, v) -> targetDataSources.put(k.toUpperCase(), initDataSource(k, v)));

        if (CollectionUtils.isEmpty(targetDataSources)) {
            throw new IllegalStateException("多数据源配置非法！");
        }

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        Object key = dsProperties.getPrimary().toUpperCase();
        if (!targetDataSources.containsKey(key)) {
            throw new IllegalStateException("primary数据源不存在！");
        }

        log.info("动态数据源启用，当前默认数据源为： " + key);
        myRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get(key));
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }


    public DataSource initDataSource(String prefix, DataSourceProperties properties) {
        if (!DruidCheckUtil.hasDruidPkg()) {
            log.info("实例化HikarDataSource: {}", prefix);
            return properties.initializeDataSourceBuilder().build();
        }

        if (properties.getType() == null || !properties.getType().isAssignableFrom(DruidDataSource.class)) {
            log.info("实例化HikarDataSource: {}", prefix);
            return properties.initializeDataSourceBuilder().build();
        }

        log.info("实例化DruidDataSource: {}", prefix);
        return Binder.get(environment).bindOrCreate(DsProperties.DS_PREFIX + ".datasource." + prefix, DruidDataSource.class);
    }

    /**
     * 在数据源实例化之后进行创建
     *
     * @return
     */
    @Bean
    @ConditionalOnExpression(value = "T(ink.whi.core.dal.DruidCheckUtil).hasDruidPkg()")
    public ServletRegistrationBean<?> druidStatViewServlet() {
        //先配置管理后台的servLet，访问的入口为/druid/
        ServletRegistrationBean<?> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "");
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }
}
