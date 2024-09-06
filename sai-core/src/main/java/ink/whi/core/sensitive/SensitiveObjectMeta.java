package ink.whi.core.sensitive;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 敏感词类元数据
 *
 * @author qing
 * @date 2023/8/9
 */
@Data
public class SensitiveObjectMeta {
    private static final String JAVA_LANG_OBJECT = "java.lang.object";
    /**
     * 是否需要脱敏
     */
    private Boolean needSensitiveReplace;

    private String className;

    /**
     * 标注 SensitiveField 的成员
     */
    private List<SensitiveFieldMeta> sensitiveFieldMetaList;

    public static Optional<SensitiveObjectMeta> buildSensitiveObjectMeta(Object param) {
        if (isNull(param)) {
            return Optional.empty();
        }

        Class<?> clazz = param.getClass();
        SensitiveObjectMeta sensitiveObjectMeta = new SensitiveObjectMeta();
        sensitiveObjectMeta.setClassName(clazz.getName());

        List<SensitiveFieldMeta> sensitiveFieldMetaList = newArrayList();
        sensitiveObjectMeta.setSensitiveFieldMetaList(sensitiveFieldMetaList);
        boolean sensitiveField = parseAllSensitiveFields(clazz, sensitiveFieldMetaList);
        // 有敏感字段时才需要过滤
        sensitiveObjectMeta.setNeedSensitiveReplace(sensitiveField);
        return Optional.of(sensitiveObjectMeta);
    }


    private static boolean parseAllSensitiveFields(Class<?> clazz, List<SensitiveFieldMeta> sensitiveFieldMetaList) {
        Class<?> tempClazz = clazz;
        boolean hasSensitiveField = false;
        while (nonNull(tempClazz) && !JAVA_LANG_OBJECT.equalsIgnoreCase(tempClazz.getName())) {
            for (Field field : tempClazz.getDeclaredFields()) {
                SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
                if (nonNull(sensitiveField)) {
                    SensitiveFieldMeta sensitiveFieldMeta = new SensitiveFieldMeta();
                    sensitiveFieldMeta.setName(field.getName());
                    sensitiveFieldMeta.setBindField(sensitiveField.bind());
                    sensitiveFieldMetaList.add(sensitiveFieldMeta);
                    hasSensitiveField = true;
                }
            }
            tempClazz = tempClazz.getSuperclass();
        }
        return hasSensitiveField;
    }

    /**
     * 敏感词字段
     */
    @Data
    public static class SensitiveFieldMeta {
        /**
         * 字段名; 默认根据字段名，找db中同名的字段
         */
        private String name;

        /**
         * 绑定的数据库字段别名
         */
        private String bindField;
    }
}
