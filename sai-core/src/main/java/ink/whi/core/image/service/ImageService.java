package ink.whi.core.image.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author: qing
 * @Date: 2023/5/1
 */
@Component
public interface ImageService {
    String mdImgReplace(String content);

    String saveImg(String img);
}
