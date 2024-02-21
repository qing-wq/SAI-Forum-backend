package ink.whi.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qing
 * @date 2023/4/27
 */
@AutoConfiguration
@ComponentScan("ink.whi.service")
@MapperScan(basePackages = {
        "ink.whi.service.article.repo.mapper",
        "ink.whi.service.user.repo.mapper",
        "ink.whi.service.comment.repo.mapper",
        "ink.whi.service.statistics.repo.mapper",
        "ink.whi.service.notify.repo.mapper",})
public class ServiceAutoConfig {
}
