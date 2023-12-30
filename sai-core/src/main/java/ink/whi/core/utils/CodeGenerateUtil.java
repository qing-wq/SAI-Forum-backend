package ink.whi.core.utils;

import java.util.Random;

/**
 * @author: qing
 * @Date: 2023/12/5
 */
public class CodeGenerateUtil {

    /**
     * 生成四位随机验证码
     * @return
     */
    public static String genCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000);
    }
}
