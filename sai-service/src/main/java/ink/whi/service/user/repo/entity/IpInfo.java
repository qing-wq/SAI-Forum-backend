package ink.whi.service.user.repo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ip信息
 *
 * @author qing
 * @date 2023/4/26
 */
@Data
public class IpInfo implements Serializable {
    private static final long serialVersionUID = -4612222921661930429L;

    private String firstIp;

    private String firstRegion;

    private String latestIp;

    private String latestRegion;
}