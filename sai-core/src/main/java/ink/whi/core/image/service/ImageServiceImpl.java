package ink.whi.core.image.service;

import com.github.hui.quick.plugin.base.constants.MediaType;
import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.core.image.oss.ImageUploader;
import ink.whi.core.utils.MdImgLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: qing
 * @Date: 2023/5/1
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageUploader imageUploader;

    private static final MediaType[] STATIC_IMG_TYPE = new MediaType[]{MediaType.ImagePng, MediaType.ImageJpg, MediaType.ImageWebp, MediaType.ImageGif};

    /**
     * 外网图片转存缓存
     */
    private LoadingCache<String, String> imgReplaceCache = CacheBuilder.newBuilder().maximumSize(300).expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
        @Override
        public String load(String img) {
            try {
                InputStream stream = FileReadUtil.getStreamByFileName(img);
                URI uri = URI.create(img);
                String path = uri.getPath();
                int index = path.lastIndexOf(".");
                String fileType = null;
                if (index > 0) {
                    // 从url中获取文件类型
                    String urlFileType = path.substring(index + 1);
                    if (!(validateStaticImg(urlFileType) == null)) {
                        fileType = urlFileType;
                    }
                }
                return imageUploader.upload(stream, fileType);
            } catch (Exception e) {
                log.error("外网图片转存异常! img:{}", img, e);
                return "";
            }
        }
    });

    @Override
    public String mdImgReplace(String content) {
        if (content == null) {
            return null;
        }
        List<MdImgLoader.MdImg> imgList = MdImgLoader.loadImgs(content);
        for (MdImgLoader.MdImg img : imgList) {
            String newImg = saveImg(img.getUrl());
            content = StringUtils.replace(content, img.getOrigin(), "![" + img.getDesc() + "](" + newImg + ")");
        }
        return content;
    }

    @Override
    public String saveImg(HttpServletRequest request) {
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest req) {
            file = req.getFile("image");
        }
        if (file == null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "缺少需要上传的图片");
        }

        String fileType = validateStaticImg(file.getContentType());
        if (fileType == null) {
            throw BusinessException.newInstance(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文件格式不支持");
        }

        try {
            return imageUploader.upload(file.getInputStream(), fileType);
        } catch (IOException e) {
            log.error("图片上传出错：", e);
            throw BusinessException.newInstance(StatusEnum.UPLOAD_PIC_FAILED);
        }

    }

    @Override
    public String saveImg(String img) {
        if (imageUploader.uploadIgnore(img)) {
            // 已经转存过，不需要再次转存；非http图片，不处理
            return img;
        }

        try {
            String ans = imgReplaceCache.get(img);
            if (StringUtils.isBlank(ans)) {
                throw BusinessException.newInstance(StatusEnum.UPLOAD_PIC_FAILED);
            }
            return ans;
        } catch (Exception e) {
            return buildUploadFailImgUrl(img);
        }
    }

    private String buildUploadFailImgUrl(String img) {
        return img.contains("saveError") ? img : img + "?&cause=saveError!";
    }

    /**
     * 图片格式校验
     *
     * @param mime 图片类型
     * @return
     */
    private String validateStaticImg(String mime) {
        for (MediaType type : STATIC_IMG_TYPE) {
            if (type.getMime().equals(mime)) {
                return type.getExt();
            }
        }
        if ("svg".equalsIgnoreCase(mime)) {
            return "svg";
        }

        if (mime.contains(MediaType.ImageJpg.getExt())) {
            mime = mime.replace("jpg", "jpeg");
        }
        return null;
    }
}
