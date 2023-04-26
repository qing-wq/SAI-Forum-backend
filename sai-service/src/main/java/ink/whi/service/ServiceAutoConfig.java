package ink.whi.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author YiHui
 * @date 2022/7/6
 */
@Configuration
@ComponentScan("ink.whi.service")
@MapperScan(basePackages = {
        "ink.whi.service.article.repo.mapper",
        "ink.whi.service.user.repo.mapper",
        "ink.whi.service.comment.repo.mapper",
        "ink.whi.service.config.repo.mapper",
        "ink.whi.service.statistics.repo.mapper",
        "ink.whi.service.notify.repo.mapper",})
public class ServiceAutoConfig {
}
