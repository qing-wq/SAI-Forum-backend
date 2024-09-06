package ink.whi.core.sensitive;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author: qing
 * @Date: 2024/7/31
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
@Component
// 只要不是false都生效
@ConditionalOnProperty(prefix = "sensitive", name = "enabled")
public class SensitiveInterceptor implements Interceptor {

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> results = (List<Object>) invocation.proceed();

        if (results.isEmpty()) {
            return results;
        }
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(resultSetHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");

        Optional<Object> firstOpt = results.stream().filter(Objects::nonNull).findFirst();

        if (firstOpt.isEmpty()) {
            return results;
        }
        Object firstObject = firstOpt.get();

        SensitiveObjectMeta sensitiveObjectMeta = findSensitiveObjectMeta(firstObject);

        replaceSensitiveResults(results, mappedStatement, sensitiveObjectMeta);
        return results;
    }

    /**
     * 敏感词替换
     * @param results
     * @param mappedStatement
     * @param sensitiveObjectMeta
     */
    private void replaceSensitiveResults(Collection<Object> results, MappedStatement mappedStatement, SensitiveObjectMeta sensitiveObjectMeta) {
        if (!sensitiveObjectMeta.getNeedSensitiveReplace() || sensitiveObjectMeta.getSensitiveFieldMetaList() == null) {
            return;
        }
        for (Object obj : results) {

            final MetaObject objMetaObject = mappedStatement.getConfiguration().newMetaObject(obj);
            sensitiveObjectMeta.getSensitiveFieldMetaList().forEach(i -> {
                Object value = objMetaObject.getValue(StringUtils.isBlank(i.getBindField()) ? i.getName() : i.getBindField());
                if (value == null) {
                    return;
                } else if (value instanceof String) {
                    // field 为 String 类型
                    String strValue = (String) value;
                    String processVal = sensitiveService.replace(strValue);
                    objMetaObject.setValue(i.getName(), processVal);
                } else if (value instanceof Collection) {
                    // Collection<Object> 类型
                    Collection<Object> listValue = (Collection) value;
                    if (CollectionUtils.isNotEmpty(listValue)) {
                        Optional<Object> firstValOpt = listValue.stream().filter(Objects::nonNull).findFirst();
                        if (firstValOpt.isPresent()) {
                            SensitiveObjectMeta valSensitiveObjectMeta = findSensitiveObjectMeta(firstValOpt.get());
                            if (Boolean.TRUE.equals(valSensitiveObjectMeta.getNeedSensitiveReplace()) && CollectionUtils.isNotEmpty(valSensitiveObjectMeta.getSensitiveFieldMetaList())) {
                                // 巧妙递归
                                replaceSensitiveResults(listValue, mappedStatement, valSensitiveObjectMeta);
                            }
                        }
                    }
                } else if (!ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
                    // Object 类型
                    SensitiveObjectMeta valSensitiveObjectMeta = findSensitiveObjectMeta(value);
                    if (Boolean.TRUE.equals(valSensitiveObjectMeta.getNeedSensitiveReplace()) && CollectionUtils.isNotEmpty(valSensitiveObjectMeta.getSensitiveFieldMetaList())) {
                        replaceSensitiveResults(newArrayList(value), mappedStatement, valSensitiveObjectMeta);
                    }
                }
            });
        }
    }

    /**
     * 查找查询实体中标注的敏感字段
     * @param firstObject
     * @return
     */
    private SensitiveObjectMeta findSensitiveObjectMeta(Object firstObject) {
        Optional<SensitiveObjectMeta> sensitiveObjectMeta = SensitiveObjectMeta.buildSensitiveObjectMeta(firstObject);
        return sensitiveObjectMeta.get();
    }

    /**
     * 生成动态代理对象
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
