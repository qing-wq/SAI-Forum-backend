package ink.whi.core.image.oss.Impl;

import ink.whi.core.image.oss.ImageUploader;

import java.io.InputStream;

/**
 * TODO
 * @author: qing
 * @Date: 2024/7/29
 */
public class OssStorageWrapper implements ImageUploader {
    @Override
    public boolean uploadIgnore(String img) {
        return false;
    }

    @Override
    public String upload(InputStream stream, String fileType) {
        return "";
    }
}
