package ink.whi.web.hook.listener;

import ink.whi.api.model.event.ConfigRefreshEvent;
import ink.whi.core.autoconf.DynamicConfigContainer;
import ink.whi.core.autoconf.apollo.SpringValueRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 配置刷新事件监听
 *
 * @author qing
 * @date 2023/09/14
 */
@Service
public class ConfigRefreshEventListener implements ApplicationListener<ConfigRefreshEvent> {
    @Autowired
    private DynamicConfigContainer dynamicConfigContainer;

    /**
     * 监听配置变更事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ConfigRefreshEvent event) {
        dynamicConfigContainer.reloadConfig();
        // 参考apollo配置更新
        SpringValueRegistry.updateValue(event.getKey());
    }
}
