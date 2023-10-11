package ink.whi.api.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author: qing
 * @Date: 2023/10/11
 */
@Getter
public enum DraftsTypeEnum {

    COMMON(0, "普通草稿"),
    ARTICLE(1, "文章草稿");

    DraftsTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final Integer code;
    private final String desc;
}
