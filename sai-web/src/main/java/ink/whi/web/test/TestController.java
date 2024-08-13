package ink.whi.web.test;

import cn.hutool.json.JSONObject;
import ink.whi.core.autoconf.DynamicConfigContainer;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.core.utils.JsonUtil;
import ink.whi.core.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

/**
 * 用于一些功能测试的入口
 *
 * @author qing
 * @date 2023/3/19
 */
@Slf4j
@RestController
@RequestMapping(path = "test")
public class TestController {

    /**
     * 打印配置信息
     *
     * @param beanName
     * @return
     */
    @GetMapping("print/{beanName}")
    public String printInfo(@PathVariable String beanName) throws Exception {
        Object bean = SpringUtil.getBeanOrNull(beanName);
        if (bean == null) {
            try {
                Class<?> clz = ClassUtils.forName(beanName, this.getClass().getClassLoader());
                bean = SpringUtil.getBean(clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (AopUtils.isCglibProxy(bean)) {
            return printProxyFields(bean);
        }

        return JsonUtil.toStr(bean);
    }

    private String printProxyFields(Object proxy) {
        Class clz = ProxyUtils.getUserClass(proxy);
        Field[] fields = clz.getDeclaredFields();
        JSONObject obj = new JSONObject();
        for (Field f : fields) {
            f.setAccessible(true);
            obj.put(f.getName(), ReflectionUtils.getField(f, proxy));
        }
        return obj.toString();
    }


    /**
     * 刷新global_config动态配置
     *
     * @return
     */
    @Permission(role = UserRole.ADMIN)
    @GetMapping("refresh/config")
    public String refreshConfig() {
        DynamicConfigContainer configContainer = SpringUtil.getBean(DynamicConfigContainer.class);
        configContainer.forceRefresh();
        return JsonUtil.toStr(configContainer.getCache());
    }
}