package ink.whi.api.model.vo.mail;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmailVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8560585303684975482L;

    private String to;

    private String title = "欢迎注册";

    private String content = "";
}
