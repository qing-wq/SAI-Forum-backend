package ink.whi.core.image.oss.Impl;

import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import ink.whi.api.model.enums.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.core.config.ImageProperties;
import ink.whi.core.image.oss.ImageUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 本地文件上传实现类
 *
 * @author: qing
 * @Date: 2023/5/1
 */
@Slf4j
@Component
@ConditionalOnExpression(value = "#{'local'.equals(environment.getProperty('image.oss.type'))}")
public class LocalStorageWrapper implements ImageUploader {

    @Autowired
    private ImageProperties imageProperties;
    private final Random random;

    public LocalStorageWrapper() {
        random = new Random();
    }
    /**
     * 判断文件是否需要上传
     * @param img
     * @return
     */
    @Override
    public boolean uploadIgnore(String img) {
        if (StringUtils.isNotBlank(imageProperties.getCdnHost()) && img.startsWith(imageProperties.getCdnHost())) {
            return true;
        }

        // 如果是oss的图片，也不需要转存
        if (StringUtils.isNotBlank(imageProperties.getOss().getHost()) && img.startsWith(imageProperties.getOss().getHost())) {
            return true;
        }

        return !img.startsWith("http");
    }

    /**
     * 上传文件
     * @param input
     * @param fileType
     * @return
     */
    @Override
    public String upload(InputStream input, String fileType) {
        try {
            if (fileType == null) {
                // 根据魔数判断文件类型
                byte[] bytes = StreamUtils.copyToByteArray(input);
                input = new ByteArrayInputStream(bytes);
                fileType = getFileType((ByteArrayInputStream) input, fileType);
            }

            String path = imageProperties.getAbsTmpPath() + imageProperties.getWebImgPath();
            String fileName = genTmpFileName();

            FileWriteUtil.FileInfo file = FileWriteUtil.saveFileByStream(input, path, fileName, fileType);
            return imageProperties.buildImgUrl(imageProperties.getWebImgPath() + file.getFilename() + "." + file.getFileType());
        } catch (Exception e) {
            log.error("Parse img from httpRequest to BufferedImage error! e:", e);
            throw BusinessException.newInstance(StatusEnum.UPLOAD_PIC_FAILED);
        }
    }

    /**
     * 获取文件临时名称
     *
     * @return
     */
    private String genTmpFileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS")) + "_" + random.nextInt(100);
    }
}
