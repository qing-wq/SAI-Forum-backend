package ink.whi.service.config.repo.params;

import ink.whi.api.model.vo.page.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SearchGlobalConfigParams extends PageParam {

    private String key;
    private String value;
    private String comment;
}
