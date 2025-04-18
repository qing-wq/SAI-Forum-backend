package ink.whi.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * markdown文本中的图片识别
 *
 * @author qing
 * @date 2022/11/24
 */
public class MdImgLoader {
    private static Pattern IMG_PATTERN = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)");

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MdImg {
        /**
         * 原始文本
         */
        private String origin;
        /**
         * 图片描述
         */
        private String desc;
        /**
         * 图片地址
         */
        private String url;
    }

    public static List<MdImg> loadImgs(String content) {
        Matcher matcher = IMG_PATTERN.matcher(content);
        List<MdImg> list = new ArrayList<>();
        while (matcher.find()) {
            // 匹配到的字符串，图片名，图片地址
            list.add(new MdImg(matcher.group(0), matcher.group(1), matcher.group(2)));
        }
        return list;
    }
}
