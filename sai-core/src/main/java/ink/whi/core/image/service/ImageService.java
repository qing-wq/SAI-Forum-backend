package ink.whi.core.image.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qing
 * @Date: 2023/5/1
 */
@Component
public interface ImageService {
    String mdImgReplace(String content);

    /**
     * 转存图片
     * @param img
     * @return
     */
    String saveImg(String img);

    String saveImg(HttpServletRequest request);
}
