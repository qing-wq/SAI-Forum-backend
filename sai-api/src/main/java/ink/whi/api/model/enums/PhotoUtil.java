package ink.whi.api.model.enums;

/**
 * 头像工具类
 * @author: qing
 * @Date: 2024/11/18
 */
public class PhotoUtil {

    /**
     * 生成随机头像
     * @return
     */
    public static String genPhoto() {
        String url = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_%d.png";
        // 生成一个随机数，范围1-27
        int random = (int) (Math.random() * 27) + 1;
        return String.format(url, random);
    }
}
