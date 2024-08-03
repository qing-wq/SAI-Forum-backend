package ink.whi.api.model.vo.config.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GlobalConfigDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String keywords;
    private String value;
    private String comment;
}
