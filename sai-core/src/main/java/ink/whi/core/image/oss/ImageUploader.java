package ink.whi.core.image.oss;

import java.io.InputStream;

/**
 * @author: qing
 * @Date: 2023/5/1
 */
public interface ImageUploader {
    boolean uploadIgnore(String img);

    String upload(InputStream stream, String fileType);
}
