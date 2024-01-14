package ink.whi.service.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private Configuration configuration;

    /**
     * 生成邮箱内容
     *
     * @return
     */
    @Override
    public String generateMailContent(String code, String email) {
        // 支持html模板
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("code", code);

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), map);
        } catch (IOException | TemplateException e) {
            log.error("create mail template error: {}", e.getMessage());
            throw BusinessException.newInstance(StatusEnum.UNEXPECT_ERROR, "生成邮箱模板失败");
        }
    }
}
