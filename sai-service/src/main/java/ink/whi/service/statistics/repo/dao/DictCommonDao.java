package ink.whi.service.statistics.repo.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.enums.PushStatusEnum;
import ink.whi.service.statistics.repo.entity.DictCommonDO;
import ink.whi.service.statistics.repo.mapper.DictCommonMapper;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/10/18
 */
@Repository
public class DictCommonDao extends ServiceImpl<DictCommonMapper, DictCommonDO> {
    public static final String REVIEW = "review";

    public String getValue(String key) {
        DictCommonDO dict = lambdaQuery().eq(DictCommonDO::getTypeCode, key)
                .one();
        return dict.getDictCode();
    }

    public void setValue(String key, String value) {
        DictCommonDO record = lambdaQuery().eq(DictCommonDO::getTypeCode, key).one();
        if (record == null) {
            record = new DictCommonDO();
            record.setTypeCode(key);
            record.setDictCode(value);
            save(record);
        } else {
            record.setDictCode(value);
            updateById(record);
        }
    }

    public boolean review() {
        String review = getValue(REVIEW);
        return Boolean.parseBoolean(review);
    }
}
