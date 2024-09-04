package ink.whi.core.sensitive;

import cn.hutool.core.util.BooleanUtil;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import ink.whi.core.config.SensitiveProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词替换服务
 * @author: qing
 * @Date: 2024/7/29
 */
@Service
public class SensitiveService {

    @Autowired
    private SensitiveProperties sensitiveProperties;

    private volatile SensitiveWordBs sensitiveWordBs;

    @PostConstruct
    public void init() {
        sensitiveWordBs = SensitiveWordBs.newInstance().init();
    }

    /**
     * 是否包含敏感词
     * @param str
     * @return
     */
    public boolean contains(String str) {
        if (BooleanUtil.isTrue(sensitiveProperties.isEnable())) {
            return SensitiveWordHelper.contains(str);
        }
        return false;
    }

    /**
     * 敏感词替换
     * @param str
     * @return
     */
    public String replace(String str) {
        if (BooleanUtil.isTrue(sensitiveProperties.isEnable())) {
            return SensitiveWordHelper.replace(str);
        }
        return str;
    }

    public List<String> findAll(String str) {
        return SensitiveWordHelper.findAll(str);
    }
}
