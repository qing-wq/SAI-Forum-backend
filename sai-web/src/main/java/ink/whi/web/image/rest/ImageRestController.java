package ink.whi.web.image.rest;

import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.core.image.service.ImageService;
import ink.whi.core.permission.Permission;
import ink.whi.core.permission.UserRole;
import ink.whi.web.image.vo.ImageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 图片上传接口
 * @author: qing
 * @Date: 2023/5/2
 */
@Slf4j
@RestController
@RequestMapping(path = "image")
@Permission(role = UserRole.LOGIN)
public class ImageRestController {

    @Autowired
    private ImageService imageService;

    /**
     * 外网图片转链
     * @param imgUrl
     * @return
     */
    @GetMapping(path = "save")
    public ResVo<ImageVo> save(@RequestParam(name = "img", defaultValue = "") String imgUrl) {
        ImageVo vo = new ImageVo();
        if (StringUtils.isBlank(imgUrl)) {
            return ResVo.ok(vo);
        }

        String url = imageService.saveImg(imgUrl);
        vo.setImagePath(url);
        return ResVo.ok(vo);
    }

    /**
     * 上传图片
     * @param request
     * @return
     */
    @PostMapping(path = "upload")
    public ResVo<ImageVo> upload(HttpServletRequest request) {
        ImageVo vo = new ImageVo();
        try {
            String imagePath = imageService.saveImg(request);
            vo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error!", e);
            return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED);
        }
        return ResVo.ok(vo);
    }
}
