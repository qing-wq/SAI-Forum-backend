package ink.whi.web.config;

import ink.whi.core.config.ImageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Administrator
 * @Date: 2023/6/4
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ImageProperties imageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // fixme：只适用windows
            String imgPath = imageProperties.getAbsTmpPath() + imageProperties.getWebImgPath();
            // fixme: 这个路径后面的\\必须要。。。
            registry.addResourceHandler("/images/**").addResourceLocations("file:D:\\tmp\\sai-forum\\images\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
