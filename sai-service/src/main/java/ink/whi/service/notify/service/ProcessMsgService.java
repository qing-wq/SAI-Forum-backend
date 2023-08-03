package ink.whi.service.notify.service;

import ink.whi.api.model.vo.notify.RabbitmqMsg;

/**
 * @author: qing
 * @Date: 2023/8/2
 */
public interface ProcessMsgService {

    void processMsg(String message, RabbitmqMsg notify);
}
