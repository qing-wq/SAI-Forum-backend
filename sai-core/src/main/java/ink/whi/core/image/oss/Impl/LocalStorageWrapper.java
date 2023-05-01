package ink.whi.core.image.oss.Impl;

import ink.whi.core.image.oss.ImageUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 本地文件上传
 *
 * @author: qing
 * @Date: 2023/5/1
 */
@Slf4j
@ConditionalOnExpression(value = "#{'local'.equals(environment.getProperty('image.oss.type'))}")
@Component
public class LocalStorageWrapper implements ImageUploader {
    @Override
    public boolean uploadIgnore(String img) {
        return false;
    }

    @Override
    public String upload(InputStream stream, String fileType) {
        return null;
    }
}
